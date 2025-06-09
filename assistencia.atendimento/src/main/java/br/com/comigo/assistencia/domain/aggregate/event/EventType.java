package br.com.comigo.assistencia.domain.aggregate.event;

import br.com.comigo.core.domain.event.Event;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum EventType {

    ATENDIMENTO_SOLICITADO(AtendimentoSolicitadoEvent.class),
    ATENDIMENTO_CONFIRMADO(AtendimentoConfirmadoEvent.class),
    ATENDIMENTO_AJUSTADO(AtendimentoAjustadoEvent.class),
    ATENDIMENTO_FINALIZADO(AtendimentoFinalizadoEvent.class),
    ATENDIMENTO_CANCELADO(AtendimentoCanceladoEvent.class);

    private final Class<? extends Event> eventClass;
}
