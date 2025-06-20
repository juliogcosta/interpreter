package com.yc.core.cqrs.application.service.event.processor;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.yc.core.cqrs.C;
import com.yc.core.cqrs.adapter.outbound.repository.EventRepository;
import com.yc.core.cqrs.adapter.outbound.repository.EventSubscriptionRepository;
import com.yc.core.cqrs.application.service.event.AsyncEventHandlerImpl;
import com.yc.core.cqrs.domain.event.EventWithId;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
 * Detalhes Importantes:
 * 
 * 1. Concorrência e Locking:
 * 
 * O FOR UPDATE SKIP LOCKED garante que apenas uma instância 
 * do processador bloqueie a subscription por vez, evitando 
 * processamento duplicado em ambientes distribuídos.
 * 
 * O @Async permite que múltiplos handlers sejam processados 
 * em paralelo (cada chamada a processNewEvents() do 
 * EventSubscriptionProcessor roda em uma thread separada).
 * 
 * 
 * 2. Transações:
 * 
 * @Transactional(Propagation.REQUIRES_NEW) no 
 * EventSubscriptionProcessor garante que cada processamento
 * ocorra em uma transação isolada.
 * 
 * Propagation.MANDATORY nos repositórios força o uso de uma 
 * transação existente (garantindo atomicidade nas operações 
 * de BD).
 * 
 * 
 * 3. Checkpointing:
 * 
 * O sistema mantém o estado de processamento usando 
 * LAST_TRANSACTION_ID e LAST_EVENT_ID, permitindo retomar 
 * de onde parou após falhas.
 * 
 * 
 * 4.Extensibilidade:
 * 
 * Novos handlers são adicionados simplesmente implementando 
 * AsyncEventHandler e registrando o bean no Spring.
 * 
 */

@DependsOn("transactionManager")
@Transactional(propagation = Propagation.REQUIRES_NEW)
@Component
@RequiredArgsConstructor
@Slf4j
public class EventSubscriptionProcessor {

    /**
     * batchSize: informa o tamanho do batch de eventos a serem processados. Isso
     * evita que se torne um problema de eficiência grave no sistema se um handler
     * novo for adicionado (caso seja obrigado) a processar todos os eventos de um
     * agregado, sendo muitos.
     * 
     */
    @Value("${event-sourcing.polling-subscriptions.batch-size}")
    private int batchSize;

    private final EventSubscriptionRepository subscriptionRepository;
    private final EventRepository eventRepository;

    @Async("eventProcessingExecutor")
    public void processNewEvents(JsonNode aggregateModel) {
        final String subscriptionName = aggregateModel.get(C.type).asText();
        log.debug("Handling new events for subscription {}", subscriptionName);

        String schemaName = aggregateModel.get(C.schema).get(C.name).asText();
        this.subscriptionRepository.createSubscriptionIfAbsent(schemaName, subscriptionName);
        this.subscriptionRepository.readCheckpointAndLockSubscription(schemaName, subscriptionName)
                .ifPresentOrElse(checkpoint -> {
                    log.debug("Acquired lock on subscription {}, checkpoint = {}", subscriptionName, checkpoint);
                    List<EventWithId> events = this.eventRepository.readEventsAfterCheckpoint(
                            checkpoint.lastProcessedTransactionId(),
                            checkpoint.lastProcessedEventId(),
                            this.batchSize,
                            aggregateModel);
                    log.debug("Fetched {} new event(s) for subscription {}", events.size(), subscriptionName);
                    if (events.isEmpty()) {

                    } else {
                        events.forEach(event -> {
                            new AsyncEventHandlerImpl(aggregateModel).handleEvent(event);
                        });
                        EventWithId lastEvent = events.get(events.size() - 1);
                        this.subscriptionRepository.updateEventSubscription(schemaName, subscriptionName,
                                lastEvent.transactionId(), lastEvent.id());
                    }
                }, () -> log.debug("Can't acquire lock on subscription {}", subscriptionName));
    }
}
