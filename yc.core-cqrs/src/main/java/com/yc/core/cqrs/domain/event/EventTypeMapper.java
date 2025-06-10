package com.yc.core.cqrs.domain.event;

public interface EventTypeMapper {

    public Class<? extends Event> getClassByEventType(String eventType);
}
