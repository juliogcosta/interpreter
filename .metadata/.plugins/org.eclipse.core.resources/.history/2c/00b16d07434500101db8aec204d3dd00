package br.com.comigo.core.application.usecase;

import br.com.comigo.core.domain.Aggregate;
import br.com.comigo.core.domain.command.Command;
import jakarta.annotation.Nonnull;

public interface CommandHandler<T extends Command> {

    public void handle(Aggregate aggregate, Command command);

    @Nonnull
    public Class<T> getCommandType();
}
