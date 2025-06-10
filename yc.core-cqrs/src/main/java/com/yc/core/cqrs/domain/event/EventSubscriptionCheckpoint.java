package com.yc.core.cqrs.domain.event;

import java.math.BigInteger;

public record EventSubscriptionCheckpoint(BigInteger lastProcessedTransactionId, long lastProcessedEventId) {
}
