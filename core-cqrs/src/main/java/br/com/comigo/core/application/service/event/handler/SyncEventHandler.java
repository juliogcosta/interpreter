package br.com.comigo.core.application.service.event.handler;

import jakarta.annotation.Nonnull;

import java.util.List;

import br.com.comigo.core.domain.Aggregate;
import br.com.comigo.core.domain.event.Event;
import br.com.comigo.core.domain.event.EventWithId;

public interface SyncEventHandler {

    public void handleEvents(List<EventWithId<Event>> events, Aggregate aggregate);

    @Nonnull
    public String getAggregateType();
}
