package com.yc.core.cqrs.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.yc.core.cqrs.C;
import com.yc.core.cqrs.domain.command.Command;
import com.yc.core.cqrs.domain.event.Event;
import com.yc.core.cqrs.error.AggregateStateException;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@ToString
@Slf4j
public class Aggregate {

    protected final UUID aggregateId;
    protected ObjectNode aggregateData;

    protected final JsonNode aggregateModel;

    @JsonIgnore
    protected final List<Event> occurredEvents = new ArrayList<>();

    protected int version;

    @JsonIgnore
    protected int baseVersion;

    public Aggregate(@NonNull UUID aggregateId, int version, JsonNode aggregateModel) {
        this.aggregateId = aggregateId;
        this.baseVersion = this.version = version;
        this.aggregateModel = aggregateModel;
        this.aggregateData = new ObjectMapper().createObjectNode();
    }

    public String getAggregateType() {
        return this.aggregateModel.get(C.type).asText();
    }

    public void loadFromHistory(List<Event> events) {
        if (this.occurredEvents.isEmpty()) {

        } else
            throw new IllegalStateException("O aggregate não tem occurredEvents");
        events.forEach(event -> {
            if (event.getVersion() <= this.version) {
                throw new IllegalStateException(
                        "A versão (%s) do evento deve ser menor ou igual à versão base do agregado (%s)"
                                .formatted(event.getVersion(), this.getNextVersion()));
            }
            this.apply(event);
            this.baseVersion = this.version = event.getVersion();
        });
    }

    protected int getNextVersion() {
        return this.version + 1;
    }

    protected void applyChange(Event event) {
        if (event.getVersion() != this.getNextVersion()) {
            throw new IllegalStateException("Event version %s doesn't match expected version %s"
                    .formatted(event.getVersion(), this.getNextVersion()));
        }
        this.apply(event);
        this.occurredEvents.add(event);
        this.version = event.getVersion();
    }

    private void apply(Event event) {
        log.info("\n > Event: {}\n", event);
        this.aggregateData.put(C.id, event.getAggregateId().toString());
        this.aggregateData.put(C.status, event.getEventStatus());
        this.aggregateData.put(C.when, event.getCreatedDate().toInstant().toString());
        JsonNode eventData = event.getEventData();
        log.info("\n > eventData[1]: {}\n", eventData);
        if (eventData != null && eventData.isObject()) {
            eventData.fields().forEachRemaining(entry -> {
                if (entry.getKey().equals(C.id)) {
                    ((ObjectNode) eventData).put(C.id, Aggregate.this.getAggregateId().toString());
                } else if (entry.getKey().equals(C.status)) {
                    ((ObjectNode) eventData).put(C.status, event.getEventStatus());
                } else if (entry.getKey().equals(C.when)) {
                    ((ObjectNode) eventData).put(C.when, event.getCreatedDate().toInstant().toString());
                } else {
                    this.aggregateData.set(entry.getKey(), entry.getValue());
                }
            });
        }
        log.info("\n > eventData[2]: {}\n", eventData);

        this.version = event.getVersion();
        log.info("\n > Event applyed [aggregate:data: {}, aggregate:version: {}]\n", this.aggregateData, this.version);
    }

    public void process(Command command) {
        log.debug("\n > Processing command {} into aggregate\n", command);
        String status = null;
        log.info("\n > aggregateData: {}\n", command.getCommandData());
        if (command.getCommandData().has(C.status)) {
            status = command.getCommandData().get(C.status).asText();
        }
        ArrayNode stateControl = (ArrayNode) command.getCommandModel().get(C.stateControl);
        log.info("\n > stateControl: {}\n", stateControl);
        if (status == null) {
            if (stateControl.size() == 0) {

            } else
                throw new AggregateStateException("Não é possível deterinar o status do agregado");
        } else {
            Boolean ok = false;
            Iterator<JsonNode> iterator = stateControl.elements();
            while (iterator.hasNext()) {
                if (iterator.next().asText().equals(status)) {
                    ok = true;
                }
            }

            if (ok) {

            } else
                throw new AggregateStateException(
                        "A transição de situação (%s) do aggregado não pode ser processada.".formatted(status));
        }

        JsonNode eventModel = this.aggregateModel.get(C.event).get(command.getCommandModel().get(C.endState).asText());
        log.info("\n from DISPONIBILIZADO to CANCELADO [{}]\n", eventModel.get(C.type));
        this.applyChange(new Event(this.aggregateId, this.getAggregateType(), eventModel, command.getCommandData(),
                this.getNextVersion()));
    }
}
