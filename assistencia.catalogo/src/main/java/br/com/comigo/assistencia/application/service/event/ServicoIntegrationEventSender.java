package br.com.comigo.assistencia.application.service.event;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.comigo.assistencia.adapter.inbound.aggregate.dto.ServicoDTO;
import br.com.comigo.assistencia.domain.aggregate.AggregateType;
import br.com.comigo.assistencia.domain.aggregate.ServicoAggregate;
import br.com.comigo.core.application.service.AggregateStore;
import br.com.comigo.core.application.service.event.handler.AsyncEventHandler;
import br.com.comigo.core.domain.Aggregate;
import br.com.comigo.core.domain.event.Event;
import br.com.comigo.core.domain.event.EventWithId;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ServicoIntegrationEventSender implements AsyncEventHandler {

    private final AggregateStore aggregateStore;

    private final RabbitTemplate rabbitTemplate;

    @Value("${db.schema}")
    private String schemaName;

    @Value("${spring.rabbitmq.assistencia-catalogo.servico.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.assistencia-catalogo.servico.events.view-update.routing-key}")
    private String routingKey;

    @Override
    public void handleEvent(EventWithId<Event> eventWithId) {
        Event event = eventWithId.event();
        Aggregate aggregate = this.aggregateStore.readAggregate(this.schemaName, AggregateType.SERVICO.toString(),
                event.getAggregateId(), event.getVersion());
        ServicoAggregate servicoAggregate = (ServicoAggregate) aggregate;

        ServicoDTO servicoDTO = new ServicoDTO(
                servicoAggregate.getAggregateId(),
                servicoAggregate.getServico().getNome(),
                servicoAggregate.getServico().getDescricao(),
                servicoAggregate.getServico().getStatus(),
                servicoAggregate.getServico().getCertificacaoIso(),
                servicoAggregate.getServico().getDataHoraDisponibilizado(),
                servicoAggregate.getServico().getDataHoraIndisponibilizado(),
                servicoAggregate.getServico().getDataHoraAjustado(),
                servicoAggregate.getServico().getDataHoraCancelado());
        this.sendServicoMessage(servicoDTO);
    }

    @SneakyThrows
    private void sendServicoMessage(ServicoDTO servicoDTO) {
        this.rabbitTemplate.convertAndSend(this.exchange, this.routingKey,
                new ObjectMapper().writeValueAsString(servicoDTO));
        log.info("With routingKey {}, sent Order message {}, to exchange {}", this.routingKey, servicoDTO,
                this.exchange);
    }

    @Nonnull
    @Override
    public String getAggregateType() {
        return AggregateType.SERVICO.toString();
    }
}
