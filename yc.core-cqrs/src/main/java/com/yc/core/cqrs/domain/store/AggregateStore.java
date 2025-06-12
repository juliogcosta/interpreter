package com.yc.core.cqrs.domain.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.yc.core.cqrs.adapter.outbound.repository.AggregateRepository;
import com.yc.core.cqrs.adapter.outbound.repository.EventRepository;
import com.yc.core.cqrs.domain.Aggregate;
import com.yc.core.cqrs.domain.event.Event;
import com.yc.core.cqrs.domain.event.EventWithId;
import com.yc.core.cqrs.error.OptimisticConcurrencyControlException;
import com.yc.core.cqrs.infrastructure.config.EventSourcingProperties;
import com.yc.core.cqrs.infrastructure.config.EventSourcingProperties.SnapshottingProperties;

import jakarta.annotation.Nullable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Component
@RequiredArgsConstructor
@Slf4j
public class AggregateStore {

    private final AggregateRepository aggregateRepository;
    private final EventRepository eventRepository;
    private final EventSourcingProperties properties;

    /**
     * O agregado é criado com duas de suas propriedades, apenas, na tabela
     * ES_AGGREGATE. Imediatamente a isso, esse mesmo agregado, é atualizado com as
     * demais propriedades, no caso dados de versão. Em seguida, o sistema recupera
     * informações de relativas ao uso de snapshots e, a partir dessas, recuperando
     * a lista de eventos ocorridos no agregado até o instante, os vinculará como
     * eventos/fatos ao agregado inserindo-os no banco de dados na tabela ES_EVENT.
     * 
     */
    public List<EventWithId> saveAggregate(Aggregate aggregate) {
        log.debug("\n > Saving aggregate {}", aggregate);

        String aggregateType = aggregate.getAggregateType();
        UUID aggregateId = aggregate.getAggregateId();
        String schemaName = aggregate.getAggregateModel().get("schema").get("name").asText();
        this.aggregateRepository.createAggregateIfAbsent(schemaName, aggregateType, aggregateId);

        int expectedVersion = aggregate.getBaseVersion();
        int newVersion = aggregate.getVersion();
        if (!this.aggregateRepository.checkAndUpdateAggregateVersion(schemaName, aggregateId, expectedVersion,
                newVersion)) {
            log.warn("Optimistic concurrency control error in aggregate {} {}: "
                            + "actual version doesn't match expected version {}",
                    aggregateType, aggregateId, expectedVersion);
            throw new OptimisticConcurrencyControlException(expectedVersion);
        }
        log.info("\n > Aggregate created!");

        SnapshottingProperties snapProperties = this.properties.getSnapshotting(aggregateType);
        List<Event> occurredEvents = aggregate.getOccurredEvents();
        log.info("\n > Occurred events {}", occurredEvents);
        List<EventWithId> events = new ArrayList<>();
        for (Event occurredEvent : occurredEvents) {
            log.info("\n > Appending event {} to aggregate of type {}", occurredEvent, aggregateType);
            EventWithId eventWithId = this.eventRepository.appendEvent(occurredEvent, aggregate.getAggregateModel());
            events.add(eventWithId);
            this.createAggregateSnapshot(snapProperties, aggregate);
        }
        return events;
    }

    private void createAggregateSnapshot(SnapshottingProperties snapshotting, Aggregate aggregate) {
        if (snapshotting.enabled() && snapshotting.nthEvent() > 1
                && aggregate.getVersion() % snapshotting.nthEvent() == 0) {
            log.info("\n > Creating {} aggregate (id: {}, version: {}) snapshot", aggregate.getAggregateType(),
                    aggregate.getAggregateId(),
                    aggregate.getVersion());
            String schemaName = aggregate.getAggregateModel().get("schema").get("name").asText();
            this.aggregateRepository.createAggregateSnapshot(schemaName, aggregate);
        }
    }

    public Aggregate readAggregate(UUID aggregateId, JsonNode aggregateModel) {
    	return this.readAggregate(aggregateId, null, aggregateModel);
    }

    public Aggregate readAggregate(@NonNull final UUID aggregateId, final @Nullable Integer version, 
    		JsonNode aggregateModel) {
    	String aggregateType = aggregateModel.get("type").asText();
        
    	log.info("\n > Reading {} aggregate {}", aggregateType, aggregateId);
        SnapshottingProperties snapshotting = this.properties.getSnapshotting(aggregateType);
        Aggregate aggregate;
        if (snapshotting.enabled()) {
            aggregate = this.readAggregateFromSnapshot(aggregateId, version, aggregateModel).orElseGet(() -> {
                log.info("\n > Aggregate {} snapshot not found", aggregateId);
                return this.readAggregateFromEvents(aggregateId, version, aggregateModel);
            });

        } else {
        	log.info("\n > Version: {}", version);
            aggregate = this.readAggregateFromEvents(aggregateId, version, aggregateModel);
        }
        log.info("\n > Aggregate read {}", aggregate);
        return aggregate;
    }

    private Optional<Aggregate> readAggregateFromSnapshot(UUID aggregateId,
            @Nullable Integer aggregateVersion, JsonNode aggregateModel) {
    	String schemaName = aggregateModel.get("schema").get("name").asText();
    	return this.aggregateRepository.readAggregateSnapshot(schemaName, aggregateId, aggregateVersion)
                .map(aggregate -> {
                    int snapshotVersion = aggregate.getVersion();
                    log.debug("\n > Read aggregate {} snapshot version {}", aggregateId, snapshotVersion);
                    if (aggregateVersion == null || snapshotVersion < aggregateVersion) {
                        var events = this.eventRepository
                                .readEvents(aggregateId, snapshotVersion, aggregateVersion, aggregateModel)
                                .stream().map(EventWithId::event).toList();
                        log.debug("\n > Read {} events after version {} for aggregate {}", events.size(), snapshotVersion,
                                aggregateId);
                        aggregate.loadFromHistory(events);
                    }
                    return aggregate;
                });
    }

    private Aggregate readAggregateFromEvents(UUID aggregateId, final @Nullable Integer aggregateVersion, 
    		JsonNode aggregateModel) {
    	String schemaName = aggregateModel.get("schema").get("name").asText();
    	log.info("\n > schemaName: {}, aggregateId: {}, aggregateVersion: {}", schemaName, aggregateId, aggregateVersion);
        List<EventWithId> eventWithIds = this.eventRepository.readEvents(aggregateId, null, 
        		aggregateVersion, aggregateModel);
        /*
         * De cada EventWithId em cada eventWithIds recupera o Event e cria a events.
         * 
         * Isso é feito a fim de reconstruir o estado do agregado a partir da lista
         * de eventos (events).
         * 
         */
        List<Event> events = eventWithIds.stream().map(EventWithId::event).toList();
        log.debug("\n Read {} events for aggregate {}", events.size(), aggregateId);
        Aggregate aggregate = new Aggregate(aggregateId, aggregateVersion, aggregateModel);
        aggregate.loadFromHistory(events);
        return aggregate;
    }
}
