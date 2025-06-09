package br.com.comigo.assistencia.domain.aggregate.event;

import br.com.comigo.core.domain.event.Event;
import br.com.comigo.core.domain.event.EventTypeMapper;
import org.springframework.stereotype.Component;

@Component
public class DefaultEventTypeMapper implements EventTypeMapper {

    @Override
    public Class<? extends Event> getClassByEventType(String eventType) {
        return EventType.valueOf(eventType).getEventClass();
    }
}
