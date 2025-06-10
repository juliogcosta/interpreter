package com.yc.core.cqrs.error;

public class OptimisticConcurrencyControlException extends AggregateStateException {

    private static final long serialVersionUID = 3082002277859337665L;

	public OptimisticConcurrencyControlException(long expectedVersion) {
        super("Actual version doesn't match expected version %s", expectedVersion);
    }
}
