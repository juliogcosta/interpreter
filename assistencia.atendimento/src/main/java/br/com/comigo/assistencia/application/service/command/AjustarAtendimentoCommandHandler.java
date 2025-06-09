package br.com.comigo.assistencia.application.service.command;

import org.springframework.stereotype.Component;

import br.com.comigo.assistencia.domain.aggregate.command.AjustarAtendimentoCommand;
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
public class AjustarAtendimentoCommandHandler implements CommandHandler<AjustarAtendimentoCommand> {

	@Override
	public void handle(Aggregate aggregate, Command command) {
		AjustarAtendimentoCommand ajustarAtendimentoCommand = (AjustarAtendimentoCommand) command;
		if (ajustarAtendimentoCommand.getServicoId() == null) {
			throw new RuntimeException(new IncompleteRegisterException(
					"O servico a ser realizado pelo atendimento parece não ter sido informado adequadamente."));
		} else if (ajustarAtendimentoCommand.getServicoNome() == null) {
			throw new RuntimeException(new IncompleteRegisterException(
					"O servico (seu nome) a ser realizado pelo atendimento parece não ter sido informado adequadamente."));
		} else if (ajustarAtendimentoCommand.getDestino() == null) {
			throw new RuntimeException(new IncompleteRegisterException(
					"O endereço de destino do atendimento parece não ter sido informado adequadamente."));
		} else if (ajustarAtendimentoCommand.getOrigem() == null) {
			throw new RuntimeException(new IncompleteRegisterException(
					"O endereço de origem do atendimento parece não ter sido informado adequadamente."));
		} else if (ajustarAtendimentoCommand.getItems() == null) {
			throw new RuntimeException(new IncompleteRegisterException(
					"Os itens do atendimento parece não terem sido informados adequadamente."));
		}
		
		aggregate.process(command);
	}

	@Nonnull
	@Override
	public Class<AjustarAtendimentoCommand> getCommandType() {
		return AjustarAtendimentoCommand.class;
	}
}
