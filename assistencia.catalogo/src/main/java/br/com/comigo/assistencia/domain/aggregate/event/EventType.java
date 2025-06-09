package br.com.comigo.assistencia.domain.aggregate.event;

import br.com.comigo.core.domain.event.Event;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum EventType {

    SERVICO_DISPONIBILIZADO(ServicoDisponibilizadoEvent.class),
    SERVICO_INDISPONIBILIZADO(ServicoIndisponibilizadoEvent.class),
    SERVICO_AJUSTADO(ServicoAjustadoEvent.class),
    SERVICO_CANCELADO(ServicoCanceladoEvent.class);

    private final Class<? extends Event> eventClass;
}
