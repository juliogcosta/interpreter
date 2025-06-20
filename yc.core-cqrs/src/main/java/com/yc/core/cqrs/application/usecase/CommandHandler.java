package com.yc.core.cqrs.application.usecase;

import com.yc.core.cqrs.domain.Aggregate;
import com.yc.core.cqrs.domain.command.Command;

public interface CommandHandler {

    public void handle(Aggregate aggregate, Command command);

    /*@Nonnull
    public String getCommandType();*/
}
