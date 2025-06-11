package com.yc.core.cqrs.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
    	return this.aggregateModel.asText("aggregateType");
    }
    
    public void loadFromHistory(List<Event> events) {
        if (!occurredEvents.isEmpty()) {
            throw new IllegalStateException("Aggregate has non-empty occurredEvents");
        }
        events.forEach(event -> {
            if (event.getVersion() <= this.version) {
                throw new IllegalStateException("Event version %s <= aggregate base version %s"
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
        log.debug("Applying event {}", event);
        this.aggregateData.put("id", event.getAggregateId().toString());
        this.aggregateData.put("status", event.getEventStatus());
        this.aggregateData.put("when", event.getCreatedDate().toInstant().toString());
        this.version = event.getVersion();
    }

    public void process(Command command) {
        log.debug("\n > processing command {} into aggregate", command);
        String status = command.getAggregateData().asText("status");
        if (status == null) {

        } else {
        	JsonNode processStateControl = command.getCommandModel().get("process-state-control");
            if (processStateControl != null && processStateControl.isArray()) {
                StreamSupport.stream(processStateControl.spliterator(), false)
                        .map(JsonNode::asText)
                        .anyMatch(s -> s.equals(status));
            } else throw new AggregateStateException("O status %s não pode ser ajustado", status);
        }
        
        JsonNode eventModel = this.aggregateModel.get("events").get(command.getCommandModel().asText("end-state"));
        this.applyChange(new Event(this.aggregateId, this.getAggregateType(), eventModel, this.aggregateData, this.baseVersion));
    }
}
