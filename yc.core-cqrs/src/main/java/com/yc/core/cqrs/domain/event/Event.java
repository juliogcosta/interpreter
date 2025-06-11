package com.yc.core.cqrs.domain.event;

import java.sql.Timestamp;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@ToString
@Slf4j
public class Event {

    protected final UUID aggregateId;
    protected final String aggregateType;
	protected final String eventType;
	protected final String eventStatus;
    protected final int version;
    protected final JsonNode eventData;
    protected final JsonNode eventModel;
    protected final Timestamp createdDate = new Timestamp(System.currentTimeMillis());

    public Event(UUID aggregateId, String aggregateType, JsonNode eventModel, JsonNode eventData, int version) {
		this.aggregateId = aggregateId;
		this.aggregateType = aggregateType;
		this.eventType = eventModel.asText("eventType");
		this.eventStatus = eventModel.asText("eventStatus");
		this.eventData = eventData.deepCopy();
		this.eventModel = eventModel;
		this.version = version;
	}

    final public UUID getAggregateId() {
		return this.aggregateId;
	}

    final public String getAggregateType() {
    	return this.aggregateType;
    }
    
    final public String getEventType() {
    	return this.eventType;
    }
    
    final public String getEventStatus() {
		return this.eventStatus;
	}
    
    final public JsonNode getEventData() {
		return this.eventData;
	}
    
    final public JsonNode getEventModel() {
		return this.eventModel;
	}
    
    final public int getVersion() {
		return this.version;
	}
    
    final public Timestamp getCreatedDate() {
		return this.createdDate;
	}
}
