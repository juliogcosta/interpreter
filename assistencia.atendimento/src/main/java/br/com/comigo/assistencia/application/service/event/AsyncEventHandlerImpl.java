package br.com.comigo.assistencia.application.service.event;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.yc.core.cqrs.application.service.event.AsyncEventHandler;
import com.yc.core.cqrs.domain.Aggregate;
import com.yc.core.cqrs.domain.event.Event;
import com.yc.core.cqrs.domain.event.EventWithId;
import com.yc.core.cqrs.domain.store.AggregateStore;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AsyncEventHandlerImpl implements AsyncEventHandler {

    private final AggregateStore aggregateStore;

    private final RabbitTemplate rabbitTemplate;

    @Value("${db.schema}")
    private String schemaName;

    @Value("${spring.rabbitmq.assistencia-atendimento.atendimento.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.assistencia-atendimento.atendimento.events.view-update.routing-key}")
    private String routingKey;

    @Override
    public void handleEvent(EventWithId eventWithId) {
        Event event = eventWithId.event();
        Aggregate aggregate = this.aggregateStore.readAggregate(this.schemaName, event.getAggregateType(), event.getAggregateId(), event.getVersion());

        this.sendMessage(aggregate.getAggregateData());
    }

    @SneakyThrows
    private void sendMessage(ObjectNode data) {
        this.rabbitTemplate.convertAndSend(this.exchange, this.routingKey, new ObjectMapper().writeValueAsString(data));
        log.info("With routingKey {}, sent Order message {}, to exchange {}", this.routingKey, data, this.exchange);
    }

	@Override
	public String getSubscriptionName() {
		return null;
	}
}
