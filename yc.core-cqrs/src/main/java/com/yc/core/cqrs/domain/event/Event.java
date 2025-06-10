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
	protected final String eventType;
	protected final String eventStatus;
    protected final int version;
    protected final JsonNode eventData;
    protected final JsonNode eventModel;
    protected final Timestamp createdDate = new Timestamp(System.currentTimeMillis());

    public Event(UUID aggregateId, JsonNode eventModel, JsonNode eventData, int version) {
		this.aggregateId = aggregateId;
		this.eventType = eventModel.asText("eventType");
		this.eventStatus = eventModel.asText("eventStatus");
		this.eventData = eventData.deepCopy();
		this.eventModel = eventModel;
		this.version = version;
	}
    
    public UUID getAggregateId() {
		return this.aggregateId;
	}
    
    public String getEventType() {
    	return this.eventType;
    }
    
    public String getEventStatus() {
		return eventStatus;
	}
    
    public JsonNode getEventData() {
		return eventData;
	}
    
    public JsonNode getEventModel() {
		return eventModel;
	}
    
    public int getVersion() {
		return version;
	}
    
    public Timestamp getCreatedDate() {
		return createdDate;
	}
}
