package br.com.comigo.assistencia.application.usecase;

import br.com.comigo.assistencia.domain.aggregate.command.DisponibilizarServicoCommand;
import br.com.comigo.core.application.usecase.CommandHandler;
import br.com.comigo.core.domain.Aggregate;

public interface DisponibilizarServicoCommandHandler extends CommandHandler<DisponibilizarServicoCommand> {

  /**
   * É aqui que defino oportunidade para vincular as regras de negócio, a fim de
   * manipular o estado do agregado, a partir do comando em questão.
   *
   */
  void handle(Aggregate aggregate, DisponibilizarServicoCommand command);

}
