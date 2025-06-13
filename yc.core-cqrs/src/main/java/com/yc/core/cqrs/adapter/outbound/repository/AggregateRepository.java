package com.yc.core.cqrs.adapter.outbound.repository;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.postgresql.util.PGobject;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yc.core.cqrs.C;
import com.yc.core.cqrs.adapter.outbound.model.ModelService;
import com.yc.core.cqrs.domain.Aggregate;

import jakarta.annotation.Nullable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Transactional(propagation = Propagation.MANDATORY)
@Repository
@RequiredArgsConstructor
public class AggregateRepository {

	private final ModelService modelService;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public void createAggregateIfAbsent(String schemaName, @NonNull String aggregateType,
            @NonNull UUID aggregateId) {
        String command = String.format("""
                        INSERT INTO %s.ES_AGGREGATE (ID, VERSION, AGGREGATE_TYPE)
                        VALUES (:aggregateId, 0, :aggregateType)
                        ON CONFLICT DO NOTHING
                        """, schemaName);
        this.jdbcTemplate.update(command, Map.of(C.aggregateId, aggregateId, C.aggregateType, aggregateType));
    }

    public boolean checkAndUpdateAggregateVersion(String schemaName, @NonNull UUID aggregateId, int expectedVersion,
            int newVersion) {
        int updatedRows = this.jdbcTemplate.update(String.format("""
                        UPDATE %s.ES_AGGREGATE
                           SET VERSION = :newVersion
                         WHERE ID = :aggregateId
                           AND VERSION = :expectedVersion
                        """, schemaName), Map.of("newVersion", newVersion, C.aggregateId, aggregateId,
                        "expectedVersion", expectedVersion));
        return updatedRows > 0;
    }

    @SneakyThrows
    public void createAggregateSnapshot(String schemaName, @NonNull Aggregate aggregate) {
        this.jdbcTemplate.update(String.format("""
                        INSERT INTO %s.ES_AGGREGATE_SNAPSHOT (AGGREGATE_ID, VERSION, JSON_DATA)
                        VALUES (:aggregateId, :version, :jsonObj::json)
                        """, schemaName), Map.of(C.aggregateId, aggregate.getAggregateId(), C.version,
                        aggregate.getVersion(), "jsonObj", this.objectMapper.writeValueAsString(aggregate)));
    }

    public Optional<Aggregate> readAggregateSnapshot(String schemaName, @NonNull UUID aggregateId,
            @Nullable Integer version) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(C.aggregateId, aggregateId);
        parameters.addValue(C.version, version, Types.INTEGER);

        return jdbcTemplate.query(String.format("""
                        SELECT a.AGGREGATE_TYPE,
                               s.JSON_DATA
                          FROM %s.ES_AGGREGATE_SNAPSHOT s
                          JOIN %s.ES_AGGREGATE a ON a.ID = s.AGGREGATE_ID
                         WHERE s.AGGREGATE_ID = :aggregateId
                           AND (:version IS NULL OR s.VERSION <= :version)
                         ORDER BY s.VERSION DESC
                         LIMIT 1
                        """, schemaName, schemaName), parameters, this::toAggregate).stream().findFirst();
    }

    @SneakyThrows
    private Aggregate toAggregate(ResultSet rs, int rowNum) {
        PGobject jsonObj = (PGobject) rs.getObject("JSON_DATA");
        JsonNode jsNode = this.objectMapper.readTree(jsonObj.getValue());
        UUID aggregateId = UUID.fromString(jsNode.get("id").asText());
        int version = jsNode.get(C.version).asInt();
        String aggregateType = rs.getString("AGGREGATE_TYPE");
        JsonNode aggregateModel = this.modelService.getModel(C.tenant).get(aggregateType);
        return new Aggregate(aggregateId, version, aggregateModel);
    }
}
