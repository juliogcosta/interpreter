package br.com.comigo.assistencia.domain.aggregate.command;

import br.com.comigo.assistencia.domain.aggregate.AggregateType;
import br.com.comigo.core.domain.command.Command;
import lombok.ToString;

import java.util.UUID;

@ToString(callSuper = true)
public final class FinalizarAtendimentoCommand extends Command {

    public FinalizarAtendimentoCommand(UUID aggregateId) {
        super(AggregateType.ATENDIMENTO.toString(), aggregateId);
    }
}