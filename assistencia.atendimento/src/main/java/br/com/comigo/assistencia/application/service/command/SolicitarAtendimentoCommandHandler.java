package br.com.comigo.assistencia.application.service.command;

import org.springframework.stereotype.Component;

import br.com.comigo.assistencia.domain.aggregate.command.SolicitarAtendimentoCommand;
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
public class SolicitarAtendimentoCommandHandler implements CommandHandler<SolicitarAtendimentoCommand> {

	@Override
	public void handle(Aggregate aggregate, Command command) {
		SolicitarAtendimentoCommand solicitarAtendimentoCommand = (SolicitarAtendimentoCommand) command;
		if (solicitarAtendimentoCommand.getClienteNome() == null) {
			throw new RuntimeException(new IncompleteRegisterException(
					"O valor de nome de Cliente parece não ter sido informado adequadamente."));
		} else if (solicitarAtendimentoCommand.getClienteTipoDeDocFiscal() == null) {
			throw new RuntimeException(new IncompleteRegisterException(
					"O valor de tipoDeDocFiscal de Cliente parece não ter sido informado adequadamente."));
		} else if (solicitarAtendimentoCommand.getClienteDocFiscal() == null) {
			throw new RuntimeException(new IncompleteRegisterException(
					"O valor de docFiscal de Cliente parece não ter sido informado adequadamente."));
		} else if (solicitarAtendimentoCommand.getClienteTelefone() == null) {
			throw new RuntimeException(new IncompleteRegisterException(
					"O valor de telefone de Cliente parece não ter sido informado adequadamente."));
		} else if (solicitarAtendimentoCommand.getTipoOcorrencia() == null) {
			throw new RuntimeException(new IncompleteRegisterException(
					"O tipo de ocorrência do atendimento parece não ter sido informado adequadamente."));
		} else if (solicitarAtendimentoCommand.getVeiculoPlaca() == null) {
			throw new RuntimeException(new IncompleteRegisterException(
					"A placa do veículo no atendimento parece não ter sido informada adequadamente."));
		} else if (solicitarAtendimentoCommand.getServicoId() == null) {
			throw new RuntimeException(new IncompleteRegisterException(
					"O servico a ser realizado pelo atendimento parece não ter sido informado adequadamente."));
		} else if (solicitarAtendimentoCommand.getServicoNome() == null) {
			throw new RuntimeException(new IncompleteRegisterException(
					"O servico (seu nome) a ser realizado pelo atendimento parece não ter sido informado adequadamente."));
		} else if (solicitarAtendimentoCommand.getBase() == null) {
			throw new RuntimeException(new IncompleteRegisterException(
					"O endereço base do atendimento parece não ter sido informado adequadamente."));
		} else if (solicitarAtendimentoCommand.getOrigem() == null) {
			throw new RuntimeException(new IncompleteRegisterException(
					"O endereço de origem do atendimento parece não ter sido informado adequadamente."));
		}
		
		aggregate.process(command);
	}

	@Nonnull
	@Override
	public Class<SolicitarAtendimentoCommand> getCommandType() {
		return SolicitarAtendimentoCommand.class;
	}
}
