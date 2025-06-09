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
public final class ServicoDisponibilizadoEvent extends Event {

    private final String nome;
    private final String descricao;
    private final Boolean certificacaoIso;

    @JsonCreator
    @Builder
    public ServicoDisponibilizadoEvent(UUID aggregateId, 
    		int version, 
    		String nome,
    		String descricao,
    		Boolean certificacaoIso) {
        super(aggregateId, version);

        this.nome = nome;
        this.descricao = descricao;
        this.certificacaoIso = certificacaoIso;
    }

    @Nonnull
    @Override
    public String getEventType() {
        return EventType.SERVICO_DISPONIBILIZADO.toString();
    }
}
