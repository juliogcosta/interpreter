package br.com.comigo.assistencia.domain.aggregate;

import br.com.comigo.core.domain.Aggregate;
import br.com.comigo.core.domain.AggregateTypeMapper;
import org.springframework.stereotype.Component;

@Component
public class DefaultAggregateTypeMapper implements AggregateTypeMapper {

    @Override
    public Class<? extends Aggregate> getClassByAggregateType(String aggregateType) {
        return AggregateType.valueOf(aggregateType).getAggregateClass();
    }
}
