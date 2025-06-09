package br.com.comigo.assistencia.domain.aggregate.command;

import br.com.comigo.assistencia.domain.aggregate.AggregateType;
import br.com.comigo.core.domain.command.Command;
import lombok.ToString;

import java.util.UUID;

@ToString(callSuper = true)
public final class IndisponibilizarServicoCommand extends Command {

    public IndisponibilizarServicoCommand(UUID aggregateId) {
        super(AggregateType.SERVICO.toString(), aggregateId);
    }
}