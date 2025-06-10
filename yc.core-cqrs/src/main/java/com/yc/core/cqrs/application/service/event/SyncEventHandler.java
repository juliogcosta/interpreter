package com.yc.core.cqrs.application.service.event;

import java.util.List;

import com.yc.core.cqrs.domain.Aggregate;
import com.yc.core.cqrs.domain.event.EventWithId;

public interface SyncEventHandler {

    public void handleEvents(List<EventWithId> events, Aggregate aggregate);
}
