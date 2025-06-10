package com.yc.core.cqrs.domain;

public interface AggregateTypeMapper {

    public Class<? extends Aggregate> getClassByAggregateType(String aggregateType);
}
