package br.com.comigo.core.application.service;

import jakarta.annotation.Nullable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.comigo.core.adapter.outbound.repository.AggregateRepository;
import br.com.comigo.core.adapter.outbound.repository.EventRepository;
import br.com.comigo.core.domain.Aggregate;
import br.com.comigo.core.domain.event.Event;
import br.com.comigo.core.domain.event.EventWithId;
import br.com.comigo.core.error.OptimisticConcurrencyControlException;
import br.com.comigo.core.infrastructure.config.EventSourcingProperties;
import br.com.comigo.core.infrastructure.config.EventSourcingProperties.SnapshottingProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Component
@RequiredArgsConstructor
@Slf4j
public class AggregateStore {

    private final AggregateRepository aggregateRepository;
    private final EventRepository eventRepository;
    private final AggregateFactory aggregateFactory;
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
    public List<EventWithId<Event>> saveAggregate(String schemaName, Aggregate aggregate) {
        log.debug("Saving aggregate {}", aggregate);

        String aggregateType = aggregate.getAggregateType();
        UUID aggregateId = aggregate.getAggregateId();
        this.aggregateRepository.createAggregateIfAbsent(schemaName, aggregateType, aggregateId);

        int expectedVersion = aggregate.getBaseVersion();
        int newVersion = aggregate.getVersion();
        if (!this.aggregateRepository.checkAndUpdateAggregateVersion(schemaName, aggregateId, expectedVersion,
                newVersion)) {
            log.warn(
                    "Optimistic concurrency control error in aggregate {} {}: "
                            + "actual version doesn't match expected version {}",
                    aggregateType, aggregateId, expectedVersion);
            throw new OptimisticConcurrencyControlException(expectedVersion);
        }
        log.info(" > aggregate created!");

        SnapshottingProperties snapProperties = this.properties.getSnapshotting(aggregateType);
        List<Event> occurredEvents = aggregate.getOccurredEvents();
        log.info(" > occurredEvents {}", occurredEvents);
        List<EventWithId<Event>> events = new ArrayList<>();
        for (Event occurredEvent : occurredEvents) {
            log.info(" > appending event {} to aggregate of type {}", occurredEvent, aggregateType);
            EventWithId<Event> eventWithId = this.eventRepository.appendEvent(schemaName, occurredEvent);
            events.add(eventWithId);
            this.createAggregateSnapshot(schemaName, snapProperties, aggregate);
        }
        return events;
    }

    private void createAggregateSnapshot(String schemaName, SnapshottingProperties snapshotting, Aggregate aggregate) {
        if (snapshotting.enabled() && snapshotting.nthEvent() > 1
                && aggregate.getVersion() % snapshotting.nthEvent() == 0) {
            log.info("Creating {} aggregate {} version {} snapshot", aggregate.getAggregateType(),
                    aggregate.getAggregateId(),
                    aggregate.getVersion());
            this.aggregateRepository.createAggregateSnapshot(schemaName, aggregate);
        }
    }

    public Aggregate readAggregate(String schemaName, String aggregateType, UUID aggregateId) {
        return this.readAggregate(schemaName, aggregateType, aggregateId, null);
    }

    public Aggregate readAggregate(String schemaName, @NonNull String aggregateType, @NonNull UUID aggregateId,
            final @Nullable Integer version) {
        log.info(" > reading {} aggregate {}", aggregateType, aggregateId);
        SnapshottingProperties snapshotting = this.properties.getSnapshotting(aggregateType);
        Aggregate aggregate;
        if (snapshotting.enabled()) {
            aggregate = this.readAggregateFromSnapshot(schemaName, aggregateId, version).orElseGet(() -> {
                log.info(" > aggregate {} snapshot not found", aggregateId);
                return this.readAggregateFromEvents(schemaName, aggregateType, aggregateId, version);
            });

        } else {
        	log.info(" > version: {}", version);
            aggregate = this.readAggregateFromEvents(schemaName, aggregateType, aggregateId, version);
        }
        log.info(" > readed aggregate {}", aggregate);
        return aggregate;
    }

    private Optional<Aggregate> readAggregateFromSnapshot(String schemaName, UUID aggregateId,
            @Nullable Integer aggregateVersion) {
        return this.aggregateRepository.readAggregateSnapshot(schemaName, aggregateId, aggregateVersion)
                .map(aggregate -> {
                    int snapshotVersion = aggregate.getVersion();
                    log.debug("Read aggregate {} snapshot version {}", aggregateId, snapshotVersion);
                    if (aggregateVersion == null || snapshotVersion < aggregateVersion) {
                        var events = this.eventRepository
                                .readEvents(schemaName, aggregateId, snapshotVersion, aggregateVersion)
                                .stream().map(EventWithId::event).toList();
                        log.debug("Read {} events after version {} for aggregate {}", events.size(), snapshotVersion,
                                aggregateId);
                        aggregate.loadFromHistory(events);
                    }
                    return aggregate;
                });
    }

    private Aggregate readAggregateFromEvents(String schemaName, String aggregateType, UUID aggregateId,
            final @Nullable Integer aggregateVersion) {
    	log.info(" > {}, {}, {}", schemaName, aggregateId, aggregateVersion);
        List<EventWithId<Event>> eventWithIds = this.eventRepository.readEvents(
                schemaName, aggregateId, null, aggregateVersion);
        // var
        /*
         * De cada EventWithId em cada eventWithIds recupera o Event e cria a events.
         * 
         * Isso é feito a fim de reconstruir o estado do agregado a partir da lista
         * de eventos (events).
         * 
         */
        List<Event> events = eventWithIds.stream().map(EventWithId::event).toList();
        log.debug("Read {} events for aggregate {}", events.size(), aggregateId);
        Aggregate aggregate = this.aggregateFactory.newInstance(aggregateType, aggregateId);
        aggregate.loadFromHistory(events);
        return aggregate;
    }
}
