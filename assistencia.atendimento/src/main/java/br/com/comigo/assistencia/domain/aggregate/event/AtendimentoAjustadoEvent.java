package br.com.comigo.assistencia.domain.aggregate.event;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;

import br.com.comigo.assistencia.domain.aggregate.entity.Item;
import br.com.comigo.common.model.records.Endereco;
import br.com.comigo.core.domain.event.Event;
import jakarta.annotation.Nonnull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public final class AtendimentoAjustadoEvent extends Event {

	private final String servicoId;
	private final String servicoNome;
    private final Endereco origem;
    private final Endereco destino;
	private final String descricao;
    private final List<Item> items;

    @JsonCreator
    @Builder
    public AtendimentoAjustadoEvent(UUID aggregateId, int version, String servicoId, String servicoNome, Endereco origem, Endereco destino, String descricao, List<Item> items) {
        super(aggregateId, version);

        this.servicoId = servicoId;
        this.servicoNome = servicoNome;
        this.origem = origem;
        this.destino = destino;
        this.descricao = descricao;
        this.items = List.copyOf(items);
    }

    @Nonnull
    @Override
    public String getEventType() {
        return EventType.ATENDIMENTO_AJUSTADO.toString();
    }
}
