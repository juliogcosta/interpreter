package com.yc.core.cqrs.adapter.outbound.repository;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.postgresql.util.PGobject;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yc.core.cqrs.C;
import com.yc.core.cqrs.domain.event.Event;
import com.yc.core.cqrs.domain.event.EventWithId;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional(propagation = Propagation.MANDATORY)
@Repository
@RequiredArgsConstructor
public class EventRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public EventWithId appendEvent(@NonNull Event event, JsonNode aggregateModel) {
        String schemaName = aggregateModel.get(C.schema).get(C.name).asText();
        if (!schemaName.matches("[a-zA-Z0-9_]+")) { // Permite apenas letras, números e _
                throw new IllegalArgumentException("Nome de schema inválido: " + schemaName);
        }
        List<EventWithId> result = this.jdbcTemplate.query(String.format("""
                        INSERT INTO %s.ES_EVENT (TRANSACTION_ID, AGGREGATE_ID, AGGREGATE_TYPE, VERSION, EVENT_TYPE, JSON_DATA)
                        VALUES (pg_current_xact_id(), :aggregateId, :aggregateType, :version, :eventType, :jsonObj::json)
                        RETURNING ID, TRANSACTION_ID::text, EVENT_TYPE, JSON_DATA
                        """, schemaName),
                        Map.of(
                        		"aggregateId", event.getAggregateId(), 
                        		"aggregateType", event.getAggregateType(), 
                        		"version", event.getVersion(),
                                "eventType", event.getEventType(), 
                                "jsonObj", this.objectMapper.writeValueAsString(event.getEventData())),
                        (rs, rowNum) -> toEvent(rs, rowNum, aggregateModel));
        return result.get(0);
    }

    public List<EventWithId> readEvents(@NonNull UUID aggregateId,
            final @Nullable Integer fromVersion, final @Nullable Integer toVersion,
            JsonNode aggregateModel) {
        String schemaName = aggregateModel.get(C.schema).get(C.name).asText();
        if (!schemaName.matches("[a-zA-Z0-9_]+")) { // Permite apenas letras, números e _
                throw new IllegalArgumentException("Nome de schema inválido: " + schemaName);
        }
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("aggregateId", aggregateId);
        parameters.addValue("fromVersion", fromVersion, Types.INTEGER);
        parameters.addValue("toVersion", toVersion, Types.INTEGER);

        return this.jdbcTemplate.query(String.format("""
                        SELECT ID,
                               TRANSACTION_ID::text,
                               AGGREGATE_TYPE,
                               EVENT_TYPE,
                               JSON_DATA
                          FROM %s.ES_EVENT
                         WHERE AGGREGATE_ID = :aggregateId
                           AND (:fromVersion IS NULL OR VERSION > :fromVersion)
                           AND (:toVersion IS NULL OR VERSION <= :toVersion)
                         ORDER BY VERSION ASC
                        """, schemaName), parameters, (rs, rowNum) -> toEvent(rs, rowNum, aggregateModel));
    }

    public List<EventWithId> readEventsAfterCheckpoint(@NonNull BigInteger lastProcessedTransactionId, 
    		long lastProcessedEventId, int batchSize, JsonNode aggregateModel) {
        String schemaName = aggregateModel.get(C.schema).get(C.name).asText();
        String aggregateType = aggregateModel.get(C.type).asText();
    	if (!schemaName.matches("[a-zA-Z0-9_]+")) { // Permite apenas letras, números e _
            throw new IllegalArgumentException("Nome de schema inválido: " + schemaName);
        }
        return this.jdbcTemplate.query(
                String.format("""
                        SELECT e.ID,
                               e.TRANSACTION_ID::text,
                               e.AGGREGATE_TYPE,
                               e.EVENT_TYPE,
                               e.JSON_DATA
                          FROM %s.ES_EVENT e
                          JOIN %s.ES_AGGREGATE a on a.ID = e.AGGREGATE_ID
                         WHERE a.AGGREGATE_TYPE = :aggregateType
                           AND (e.TRANSACTION_ID, e.ID) > (:lastProcessedTransactionId::xid8, :lastProcessedEventId)
                           AND e.TRANSACTION_ID < pg_snapshot_xmin(pg_current_snapshot())
                         ORDER BY e.TRANSACTION_ID ASC, e.ID ASC
                         LIMIT :batchSize
                        """,
                        schemaName, schemaName),
                Map.of("aggregateType", aggregateType, "lastProcessedTransactionId",
                                lastProcessedTransactionId.toString(), "lastProcessedEventId",
                                lastProcessedEventId, "batchSize", batchSize),
                (rs, rowNum) -> toEvent(rs, rowNum, aggregateModel));
    }

    @SneakyThrows
    private EventWithId toEvent(ResultSet rs, int rowNum, JsonNode aggregateModel) {
        long id = rs.getLong("ID");
        String transactionId = rs.getString("TRANSACTION_ID");
        UUID aggregateId = UUID.fromString(rs.getString("AGGREGATE_ID"));
        String aggregateType = rs.getString("AGGREGATE_TYPE");
        Integer version = rs.getInt("VERSION");
        String eventType = rs.getString("EVENT_TYPE");
        JsonNode eventModel = aggregateModel.get("event").get(eventType);
        PGobject jsonObj = (PGobject) rs.getObject("JSON_DATA");
        String json = jsonObj.getValue();
        JsonNode eventData = this.objectMapper.readTree(json);
        Event event = new Event(aggregateId, aggregateType, eventModel, eventData, version);
        log.info("\n > Event loaded: {}", event);
        return new EventWithId(id, new BigInteger(transactionId), event);
    }
}
