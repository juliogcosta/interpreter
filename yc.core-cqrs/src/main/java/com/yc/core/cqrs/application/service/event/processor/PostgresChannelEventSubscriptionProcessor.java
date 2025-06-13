package com.yc.core.cqrs.application.service.event.processor;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.postgresql.PGNotification;
import org.postgresql.jdbc.PgConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.yc.core.cqrs.C;
import com.yc.core.cqrs.adapter.outbound.model.ModelService;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@ConditionalOnProperty(name = "event-sourcing.subscriptions", havingValue = "postgres-channel")
@RequiredArgsConstructor
@Slf4j
public class PostgresChannelEventSubscriptionProcessor {

    /**
     * batchSize: informa o tamanho do batch de eventos a serem processados. Isso
     * evita que se torne um problema de eficiência grave no sistema se um handler
     * novo for adicionado (caso seja obrigado) a processar todos os eventos de um
     * agregado, sendo muitos.
     * 
     */
    @Value("${event-sourcing.polling-subscriptions.batch-size}")
    private int batchSize;

    private final EventSubscriptionProcessor eventSubscriptionProcessor;
    private final DataSourceProperties dataSourceProperties;
    private final ExecutorService executor = newExecutor();
    private CountDownLatch latch = new CountDownLatch(0);
    private Future<?> future = CompletableFuture.completedFuture(null);
    private volatile PgConnection connection;
	private final ModelService modelService;

    /**
     * 1. Por que criar um ExecutorService?
     * 
     * O ExecutorService é um mecanismo do Java para gerenciar threads de forma
     * eficiente e controlada.
     * 
     * No contexto da classe PostgresChannelEventSubscriptionProcessor, ele é usado
     * para:
     * 
     * a) Manter uma thread dedicada escutando notificações do PostgreSQL (LISTEN).
     * 
     * b) Garantir que o processamento de eventos ocorra assincronamente, sem
     * bloquear a thread principal da aplicação.
     * 
     * 
     * 
     * 2.Executors.newSingleThreadExecutor():
     * 
     * Cria um executor com apenas uma thread (.newSingle...), ideal para:
     * 
     * a) Processamento sequencial (evitar concorrência desnecessária).
     * 
     * b) Garantir que notificações do PostgreSQL sejam processadas na ordem de
     * chegada.
     * 
     * 
     * 
     * 3. Resumo:
     * 
     * O trecho de código é essencial para (1) criar uma infraestrutura de thread
     * especializada, segura e não bloqueante para o mecanismo LISTEN/NOTIFY do
     * PostgreSQL. E (2) garantir que o ciclo de vida da thread esteja alinhado com
     * o da aplicação Spring.
     * 
     */
    private static ExecutorService newExecutor() {
        CustomizableThreadFactory threadFactory = new CustomizableThreadFactory("postgres-channel-event-subscription-");
        threadFactory.setDaemon(true);
        return Executors.newSingleThreadExecutor(threadFactory);
    }

    @PostConstruct
    public synchronized void start() {
        if (this.latch.getCount() > 0) {
            return;
        }
        this.latch = new CountDownLatch(1);
        this.future = this.executor.submit(() -> {
            try {
                while (isActive()) {
                    try {
                        PgConnection conn = getPgConnection();
                        try (Statement stmt = conn.createStatement()) {
                            stmt.execute("LISTEN channel_event_notify");
                        } catch (Exception ex) {
                            try {
                                conn.close();
                            } catch (Exception suppressed) {
                                ex.addSuppressed(suppressed);
                            }
                            throw ex;
                        }

                        /*this.eventHandlers.forEach(handler -> PostgresChannelEventSubscriptionProcessor.this
                                .processNewEvents(this.schemaName, handler));*/
                        this.modelService.getModel("tenant").fields().forEachRemaining(field -> {
                			JsonNode aggregateModel = field.getValue();
                			try {
                				this.eventSubscriptionProcessor.processNewEvents(aggregateModel);
                			} catch (Exception e) {
                	            log.warn("Failed to handle new events for subscription %s".formatted(aggregateModel.get(C.type).asText()), e);
                			}
                        });

                        try {
                        	//log.info(" > 1");
                        	//int count = 0;
                        	
                            this.connection = conn;
                            while (isActive()) {
                            	log.info(" > pg subscription processor is ok.");
                                PGNotification[] notifications = conn.getNotifications(0);
                                // Unfortunately, there is no good way of interrupting a notification
                                // poll but by closing its connection.
                                if (isActive()) {

                                } else {
                                    return;
                                }
                                if (notifications != null) {
                                    for (PGNotification notification : notifications) {
                                        String aggregateType = notification.getParameter();
                                        this.eventSubscriptionProcessor.processNewEvents(this.modelService.getModel("tenant").get(aggregateType));
                                        /*this.eventHandlers.stream().filter(
                                                eventHandler -> eventHandler.getAggregateType().equals(aggregateType))
                                                .forEach(handler -> PostgresChannelEventSubscriptionProcessor.this
                                                        .processNewEvents(this.schemaName, handler));*/
                                    }
                                }
                            }
                        } finally {
                            conn.close();
                        }
                    } catch (Exception e) {
                        // The getNotifications method does not throw a meaningful message on
                        // interruption.
                        // Therefore, we do not log an error, unless it occurred while active.
                        if (isActive()) {
                            log.error("Failed to poll notifications from Postgres database", e);
                        }
                    }
                }
            } finally {
                this.latch.countDown();
            }
        });
    }

    private PgConnection getPgConnection() throws SQLException {
        return DriverManager.getConnection(this.dataSourceProperties.determineUrl(),
        		this.dataSourceProperties.determineUsername(), this.dataSourceProperties.determinePassword())
                .unwrap(PgConnection.class);
    }

    private boolean isActive() {
        if (Thread.interrupted()) {
            Thread.currentThread().interrupt();
            return false;
        }
        return true;
    }

    /*private void processNewEvents(String schemaName, AsyncEventHandler eventHandler) {
        try {
            this.eventSubscriptionProcessor.processNewEvents(schemaName, eventHandler, this.batchSize);
        } catch (Exception e) {
            log.warn("Failed to handle new events for subscription %s".formatted(eventHandler.getSubscriptionName()), e);
        }
    }*/

    @PreDestroy
    public synchronized void stop() {
        if (this.future.isDone()) {
            return;
        }
        this.future.cancel(true);
        PgConnection conn = this.connection;
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
        try {
            if (!this.latch.await(5, TimeUnit.SECONDS)) {
                throw new IllegalStateException(
                        "Failed to stop %s".formatted(PostgresChannelEventSubscriptionProcessor.class.getName()));
            }
        } catch (InterruptedException ignored) {
        }
    }
}