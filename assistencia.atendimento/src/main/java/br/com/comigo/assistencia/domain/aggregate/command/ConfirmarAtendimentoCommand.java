package br.com.comigo.assistencia.domain.aggregate.command;

import java.util.UUID;

import br.com.comigo.assistencia.adapter.inbound.aggregate.dto.AtendimentoDTO;
import br.com.comigo.assistencia.domain.aggregate.AggregateType;
import br.com.comigo.common.infrastructure.exception.IncompleteRegisterException;
import br.com.comigo.core.domain.command.Command;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public final class ConfirmarAtendimentoCommand extends Command {

	private final String prestadorId;

	public ConfirmarAtendimentoCommand(UUID aggregateId, AtendimentoDTO atendimentoDTO) {
		super(AggregateType.ATENDIMENTO.toString(), aggregateId);

		if (atendimentoDTO.prestadorId() == null) {
			throw new RuntimeException(new IncompleteRegisterException(
					"O proseguimento da operação precisa da identificacao do prestador de servico."));
		}
		
		this.prestadorId = atendimentoDTO.prestadorId();
	}
}
