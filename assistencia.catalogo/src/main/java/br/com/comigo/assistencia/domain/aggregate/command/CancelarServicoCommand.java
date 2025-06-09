package br.com.comigo.assistencia.domain.aggregate.command;

import br.com.comigo.assistencia.domain.aggregate.AggregateType;
import br.com.comigo.core.domain.command.Command;
import lombok.ToString;

import java.util.UUID;

@ToString(callSuper = true)
public final class CancelarServicoCommand extends Command {

    public CancelarServicoCommand(UUID aggregateId) {
        super(AggregateType.SERVICO.toString(), aggregateId);
    }
}
