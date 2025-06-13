package com.yc.core.cqrs.adapter.outbound.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yc.core.cqrs.domain.event.EventSubscriptionCheckpoint;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Transactional(propagation = Propagation.MANDATORY)
@Repository
@RequiredArgsConstructor
public class EventSubscriptionRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void createSubscriptionIfAbsent(String schemaName, String subscriptionName) {
        String query = String.format("""
                INSERT INTO %s.es_event_subscription (SUBSCRIPTION_NAME, LAST_TRANSACTION_ID, LAST_EVENT_ID)
                VALUES (:subscriptionName, '0'::xid8, 0)
                ON CONFLICT DO NOTHING
                """, schemaName);
        log.info("\n > query: {}\n", query);
        
    	this.jdbcTemplate.update(query, Map.of("subscriptionName", subscriptionName));
    }

    /**
     * FOR UPDATE SKIP LOCKED:
     * 
     * Com isso, apenas a instância de backend/processor que capturou as linhas de
     * ES_EVENT_SUBSCRIPTION relativas a uma subscriptionName em particular (uma
     * assinatura), poderá fazer o serviço necessário de notificar os assinantes com
     * os eventos ocorridos para o agregado e, por fim, atualizar
     * ES_EVENT_SUBSCRIPTION com informando que tal subscriptionName está notificado
     * da situaç~ao mais atual.
     * 
     */
    public Optional<EventSubscriptionCheckpoint> readCheckpointAndLockSubscription(String schemaName,
            String subscriptionName) {
    	String query = String.format("""
         SELECT LAST_TRANSACTION_ID::text,
                LAST_EVENT_ID
           FROM %s.ES_EVENT_SUBSCRIPTION
          WHERE SUBSCRIPTION_NAME = :subscriptionName
            FOR UPDATE SKIP LOCKED
         """, schemaName);
        log.info("\n > query: {}\n", query);

        return this.jdbcTemplate.query(query, Map.of("subscriptionName", subscriptionName), this::toEventSubscriptionCheckpoint)
                .stream().findFirst();
    }

    public boolean updateEventSubscription(String schemaName, String subscriptionName,
            BigInteger lastProcessedTransactionId, long lastProcessedEventId) {
        String query = String.format("""
             UPDATE %s.ES_EVENT_SUBSCRIPTION
                SET LAST_TRANSACTION_ID = :lastProcessedTransactionId::xid8,
                    LAST_EVENT_ID = :lastProcessedEventId
              WHERE SUBSCRIPTION_NAME = :subscriptionName
             """, schemaName);
        log.info("\n > query: {}\n", query);
        
    	int updatedRows = this.jdbcTemplate.update(query, Map.of("subscriptionName", subscriptionName, "lastProcessedTransactionId",
                lastProcessedTransactionId.toString(), "lastProcessedEventId", lastProcessedEventId));

    	return updatedRows > 0;
    }

    private EventSubscriptionCheckpoint toEventSubscriptionCheckpoint(ResultSet rs, int rowNum) throws SQLException {
        String lastProcessedTransactionId = rs.getString("LAST_TRANSACTION_ID");
        long lastProcessedEventId = rs.getLong("LAST_EVENT_ID");
        return new EventSubscriptionCheckpoint(new BigInteger(lastProcessedTransactionId), lastProcessedEventId);
    }
}
