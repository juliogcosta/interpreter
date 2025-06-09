package br.com.comigo.assistencia.application.service.command;

import org.springframework.stereotype.Component;

import br.com.comigo.assistencia.domain.aggregate.command.DisponibilizarServicoCommand;
import br.com.comigo.common.infrastructure.exception.IncompleteRegisterException;
import br.com.comigo.core.application.usecase.CommandHandler;
import br.com.comigo.core.domain.Aggregate;
import br.com.comigo.core.domain.command.Command;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;

/**
 * É aqui que defino regras de negócio, a fim de manipular o estado do agregado,
 * a partir do comando em questão.
 * 
 */

@Component
@Slf4j
public class DisponibilizarServicoCommandHandler implements CommandHandler<DisponibilizarServicoCommand> {

    @Override
    public void handle(Aggregate aggregate, Command command) {
        DisponibilizarServicoCommand disponibilizarServicoCommand = (DisponibilizarServicoCommand) command;
        if (disponibilizarServicoCommand.getNome() == null) {
        	throw new RuntimeException(new IncompleteRegisterException("O valor de nome do Serviço parece não ter sido informado adequadamente."));
        } else if (disponibilizarServicoCommand.getDescricao() == null) {
        	throw new RuntimeException(new IncompleteRegisterException("O valor de descricao do Serviço parece não ter sido informado adequadamente."));
        }
        aggregate.process(command);
    }

    @Nonnull
    @Override
    public Class<DisponibilizarServicoCommand> getCommandType() {
        return DisponibilizarServicoCommand.class;
    }
}
