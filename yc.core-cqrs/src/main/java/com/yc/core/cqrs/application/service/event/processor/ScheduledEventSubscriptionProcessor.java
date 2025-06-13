package com.yc.core.cqrs.application.service.event.processor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.yc.core.cqrs.C;
import com.yc.core.cqrs.adapter.outbound.model.ModelService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Essa classe implementa um Bean a ser gerenciado pelo spring e que será
 * invocado por meio de processNewEvents(). A periodicidade da invocação do
 * método é dado por propriedade de ambiente: event-sourcing.subscriptions
 * 
 */

@Component
@ConditionalOnProperty(name = "event-sourcing.subscriptions", havingValue = "polling")
@RequiredArgsConstructor
@Slf4j
public class ScheduledEventSubscriptionProcessor {
	private final EventSubscriptionProcessor eventSubscriptionProcessor;
	private final ModelService modelService;

	@Scheduled(fixedDelayString = "${event-sourcing.polling-subscriptions.polling-interval}", initialDelayString = "${event-sourcing.polling-subscriptions.polling-initial-delay}")
	public void processNewEvents() {
		this.modelService.getModel("tenant").fields().forEachRemaining(field -> {
			JsonNode aggregateModel = field.getValue();
			try {
				this.eventSubscriptionProcessor.processNewEvents(aggregateModel);
			} catch (Exception e) {
	            log.warn("Failed to handle new events for subscription %s".formatted(aggregateModel.get(C.type).asText()), e);
			}
		});
	}
}
