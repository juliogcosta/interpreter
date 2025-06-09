package br.com.comigo.assistencia.domain.aggregate.event;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;

import br.com.comigo.core.domain.event.Event;
import jakarta.annotation.Nonnull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public final class AtendimentoConfirmadoEvent extends Event {

    private final String prestadorId;

    @JsonCreator
    @Builder
    public AtendimentoConfirmadoEvent(UUID aggregateId, int version, String prestadorId) {
        super(aggregateId, version);

        this.prestadorId = prestadorId;
    }

    @Nonnull
    @Override
    public String getEventType() {
        return EventType.ATENDIMENTO_CONFIRMADO.toString();
    }
}
