package br.com.comigo.core.domain.event;

import java.math.BigInteger;

public record EventSubscriptionCheckpoint(BigInteger lastProcessedTransactionId, long lastProcessedEventId) {
}
