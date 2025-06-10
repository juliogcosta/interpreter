package com.yc.core.cqrs.domain.event;

import java.math.BigInteger;

public record EventWithId(long id, BigInteger transactionId, Event event) {
}
