package br.com.comigo.core.domain.event;

import jakarta.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public abstract class Event {

    protected final UUID aggregateId;
    protected final int version;
    protected final Timestamp createdDate = new Timestamp(System.currentTimeMillis());

    @Nonnull
    public abstract String getEventType();
}
