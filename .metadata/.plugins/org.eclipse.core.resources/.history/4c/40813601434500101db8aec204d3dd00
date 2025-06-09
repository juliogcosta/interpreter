package br.com.comigo.core.application.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import br.com.comigo.core.domain.Aggregate;
import br.com.comigo.core.domain.AggregateTypeMapper;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AggregateFactory {

    private final AggregateTypeMapper aggregateTypeMapper;

    @SneakyThrows(ReflectiveOperationException.class)
    @SuppressWarnings("unchecked")
    public <T extends Aggregate> T newInstance(String aggregateType, UUID aggregateId) {
        Class<? extends Aggregate> aggregateClass = aggregateTypeMapper.getClassByAggregateType(aggregateType);
        var constructor = aggregateClass.getDeclaredConstructor(UUID.class, Integer.TYPE);
        return (T) constructor.newInstance(aggregateId, 0);
    }
}
