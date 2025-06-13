package com.yc.core.cqrs.application.service.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.yc.core.cqrs.domain.event.EventWithId;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AsyncEventHandlerImpl {

	private final JsonNode aggregateModel;

	public AsyncEventHandlerImpl(JsonNode aggregateModel) {
		this.aggregateModel = aggregateModel;
	}
	
	public void handleEvent(EventWithId event) {
		log.info("\n > Handling event {} for aggregate {}\n", event, this.aggregateModel);
	}
}
