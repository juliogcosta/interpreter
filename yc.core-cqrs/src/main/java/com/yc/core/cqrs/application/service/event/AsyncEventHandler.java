package com.yc.core.cqrs.application.service.event;

import com.yc.core.cqrs.domain.event.EventWithId;

public interface AsyncEventHandler {

    public void handleEvent(EventWithId event);

    public String getAggregateType();

    public String getSubscriptionName();
}
