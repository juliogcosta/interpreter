package br.com.comigo.assistencia.domain.aggregate;

import br.com.comigo.core.domain.Aggregate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum AggregateType {

    SERVICO(ServicoAggregate.class);

    private final Class<? extends Aggregate> aggregateClass;
}
