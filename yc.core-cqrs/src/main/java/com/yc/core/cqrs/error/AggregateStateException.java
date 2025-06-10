package com.yc.core.cqrs.error;

import lombok.NonNull;

public class AggregateStateException extends RuntimeException {

    private static final long serialVersionUID = 2260574454558846032L;

	public AggregateStateException(@NonNull String message, Object... args) {
        super(message.formatted(args));
    }
}
