package br.com.comigo.assistencia.domain.aggregate.command;

import java.util.List;
import java.util.UUID;

import br.com.comigo.assistencia.adapter.inbound.aggregate.dto.AtendimentoDTO;
import br.com.comigo.assistencia.domain.aggregate.AggregateType;
import br.com.comigo.assistencia.domain.aggregate.entity.Item;
import br.com.comigo.common.infrastructure.exception.IncompleteRegisterException;
import br.com.comigo.common.model.records.Endereco;
import br.com.comigo.core.domain.command.Command;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public final class AjustarAtendimentoCommand extends Command {

	public final static String VERB = "AJUSTAR";
	
	private final String servicoId;
	private final String servicoNome;
	private final Endereco origem;
	private final Endereco destino;
    private final String descricao;
    private final List<Item> items;

    public AjustarAtendimentoCommand(UUID aggregateId,
            AtendimentoDTO atendimentoDTO) {
        super(AggregateType.ATENDIMENTO.toString(), aggregateId);

        this.origem = atendimentoDTO.origem();
        this.destino = atendimentoDTO.destino();
        this.servicoId = atendimentoDTO.servicoId();
        this.servicoNome = atendimentoDTO.servicoNome();
        this.descricao = atendimentoDTO.descricao();
        if (atendimentoDTO.items() == null) {
			throw new RuntimeException(new IncompleteRegisterException(
					"O proseguimento da operação precisa da lista de itens do serviço a ser realizado durante o atendimento ao cliente."));
        } else {
            this.items = atendimentoDTO.items().stream().map(item -> {
                return new Item(
                        item.nome(),
                        item.unidadeDeMedida(),
                        item.precoUnitario(),
                        item.quantidade(),
                        item.observacao());
            }).toList();
        }
    }
}
