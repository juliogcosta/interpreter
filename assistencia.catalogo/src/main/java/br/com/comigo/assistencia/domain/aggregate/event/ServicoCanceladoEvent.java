package br.com.comigo.assistencia.domain.aggregate.event;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;

import br.com.comigo.core.domain.event.Event;
import jakarta.annotation.Nonnull;
import lombok.Builder;
import lombok.ToString;

@ToString(callSuper = true)
public final class ServicoCanceladoEvent extends Event {

    @JsonCreator
    @Builder
    public ServicoCanceladoEvent(UUID aggregateId, int version) {
        super(aggregateId, version);
    }

    @Nonnull
    @Override
    public String getEventType() {
        return EventType.SERVICO_CANCELADO.toString();
    }
}
