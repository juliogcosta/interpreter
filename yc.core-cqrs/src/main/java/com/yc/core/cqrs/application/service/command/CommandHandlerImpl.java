package com.yc.core.cqrs.application.service.command;

import org.springframework.stereotype.Component;

import com.yc.core.cqrs.application.usecase.CommandHandler;
import com.yc.core.cqrs.domain.Aggregate;
import com.yc.core.cqrs.domain.command.Command;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CommandHandlerImpl implements CommandHandler {

    @Override
    public void handle(Aggregate aggregate, Command command) {
        aggregate.process(command);
    }
}
