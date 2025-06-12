package com.yc.core.cqrs.application.service.command.processor;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.yc.core.cqrs.application.service.command.CommandHandlerImpl;
import com.yc.core.cqrs.application.service.event.SyncEventHandler;
import com.yc.core.cqrs.domain.Aggregate;
import com.yc.core.cqrs.domain.command.Command;
import com.yc.core.cqrs.domain.event.EventWithId;
import com.yc.core.cqrs.domain.store.AggregateStore;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Essa é uma classe que tem por finalidade processar comandos e, ao fim,
 * disparar eventos. Importante: é uma classe genérica e, assim, desobriga o
 * desenvolvedor de implementar o mesmo código. Ela descobrirá, em tempo de
 * execução, qual o comando, qual o handler a ser utilizado, qual o agregate a
 * ser utilizado e qual o evento a ser disparado.
 * 
 * Para que isso funcione é importante que o usuario dessa implementação seja
 * capaz especilizar e implementar o handler de comando e o handler de evento.
 * 
 */
@Transactional
@Component
@RequiredArgsConstructor
@Slf4j
public class CommandProcessor {

    private final AggregateStore aggregateStore;
    private final CommandHandlerImpl commandHandler;
    private final SyncEventHandler syncEventHandler;

    /**
     * Processa um comando, recuperando o agregado associado, aplicando as regras de
     * negócio e persistindo os eventos resultantes.
     * 
     * @param command O comando a ser processado.
     * @return O agregado atualizado após o processamento do comando.
     */
    public Aggregate process(JsonNode aggregateModel, @NonNull Command command) {
        log.info("\n > Starting command processor [Command: {}]", command);

        /**
         * A invocação de "readAggregate" implica a recuperação do agregado a partid do
         * banco de dados e seus eventos associados. isso feito, o estado atual do
         * agregado será reconstituído a partir da aplicação sucessiva de cada evento
         * (atualizações) ocorridas ao longo do ciclo de vida do agregados.
         * 
         */
        UUID aggregateId = command.getAggregateId();
        Aggregate aggregate = this.aggregateStore.readAggregate(aggregateId, aggregateModel);
        log.info("\n > Aggregate read: {}", aggregate);

        if (command.getCommandModel().has("handler")) {
            /**
             * Oportunidade para a invocação de funções que executam regras de negócio, etc.
             * 
             */
        } else
            CommandProcessor.this.commandHandler.handle(aggregate, command);

        List<EventWithId> events = this.aggregateStore.saveAggregate(aggregate);
        log.info(" > events: {}", events);

        this.syncEventHandler.handleEvents(events, aggregate);

        return aggregate;
    }
}
