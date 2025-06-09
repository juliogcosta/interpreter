package br.com.comigo.assistencia.application.service.command;

import org.springframework.stereotype.Component;

import br.com.comigo.assistencia.domain.aggregate.command.ConfirmarAtendimentoCommand;
import br.com.comigo.common.infrastructure.exception.IncompleteRegisterException;
import br.com.comigo.core.application.usecase.CommandHandler;
import br.com.comigo.core.domain.Aggregate;
import br.com.comigo.core.domain.command.Command;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * É aqui que defino regras de negócio, a fim de manipular o estado do agregado,
 * a partir do comando em questão.
 * 
 */

@RequiredArgsConstructor
@Component
@Slf4j
public class ConfirmarAtendimentoCommandHandler implements CommandHandler<ConfirmarAtendimentoCommand> {

	@Override
	public void handle(Aggregate aggregate, Command command) {
		ConfirmarAtendimentoCommand confirmarAtendimentoCommand = (ConfirmarAtendimentoCommand) command;
		
		if (confirmarAtendimentoCommand.getPrestadorId() == null) {
			throw new RuntimeException(new IncompleteRegisterException(
					"O prestador a realizar o atendimento parece não ter sido informado adequadamente."));
		}
		
		aggregate.process(command);
	}

	@Nonnull
	@Override
	public Class<ConfirmarAtendimentoCommand> getCommandType() {
		return ConfirmarAtendimentoCommand.class;
	}
}
