package com.yc.core.cqrs.application.service.event;

import com.yc.core.cqrs.domain.event.EventWithId;

import jakarta.annotation.Nonnull;

public interface AsyncEventHandler {

    public void handleEvent(EventWithId event);

    @Nonnull
    public String getAggregateType();

    default String getSubscriptionName() {
        return getClass().getName();
    }
}
