package com.yc.core.cqrs.application.service.event;

import com.yc.core.cqrs.domain.event.EventWithId;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AsyncEventHandlerImpl implements AsyncEventHandler {

	private final String aggregateType;
	private final String subscriptionName;

	public AsyncEventHandlerImpl(String aggregateType, String subscriptionName) {
		this.aggregateType = aggregateType;
		this.subscriptionName = subscriptionName;
	}
	
	@Override
	public void handleEvent(EventWithId event) {
		log.info("\n > Event {} handled.", event);
	}

	@Override
	public String getAggregateType() {
		return this.aggregateType;
	}

	@Override
	public String getSubscriptionName() {
		return this.subscriptionName;
	}

}
