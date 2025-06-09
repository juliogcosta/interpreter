package br.com.comigo.assistencia.application.service.event;

import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.comigo.assistencia.adapter.inbound.aggregate.dto.AtendimentoDTO;
import br.com.comigo.assistencia.adapter.inbound.aggregate.dto.ItemDTO;
import br.com.comigo.assistencia.domain.aggregate.AggregateType;
import br.com.comigo.assistencia.domain.aggregate.AtendimentoAggregate;
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
public class AtendimentoIntegrationEventSender implements AsyncEventHandler {

    private final AggregateStore aggregateStore;

    private final RabbitTemplate rabbitTemplate;

    @Value("${db.schema}")
    private String schemaName;

    @Value("${spring.rabbitmq.assistencia-atendimento.atendimento.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.assistencia-atendimento.atendimento.events.view-update.routing-key}")
    private String routingKey;

    @Override
    public void handleEvent(EventWithId<Event> eventWithId) {
        Event event = eventWithId.event();
        Aggregate aggregate = this.aggregateStore.readAggregate(this.schemaName, AggregateType.ATENDIMENTO.toString(),
                event.getAggregateId(), event.getVersion());
        AtendimentoAggregate atendimentoAggregate = (AtendimentoAggregate) aggregate;
        
        String prestadorId = atendimentoAggregate.getAtendimento().getPrestadorId() == null 
        		? null : atendimentoAggregate.getAtendimento().getPrestadorId().toString();
        String clienteId = atendimentoAggregate.getAtendimento().getClienteId() == null 
        		? null : atendimentoAggregate.getAtendimento().getClienteId().toString();
        String servicoId = atendimentoAggregate.getAtendimento().getServicoId() == null 
        		? null : atendimentoAggregate.getAtendimento().getServicoId().toString();
        
        List<ItemDTO> items = null;
        if (atendimentoAggregate.getAtendimento().getItems() == null) {
        	items = new ArrayList<>();
        } else {
            items = atendimentoAggregate.getAtendimento().getItems().stream().map(item -> new ItemDTO(
                    item.getNome(),
                    item.getUnidadeDeMedida(),
                    item.getPrecoUnitario(),
                    item.getQuantidade(),
                    item.getObservacao())).toList();
        }
        AtendimentoDTO atendimentoDTO = new AtendimentoDTO(
        		atendimentoAggregate.getAggregateId(), 
        		prestadorId, 
        		clienteId, 
        		atendimentoAggregate.getAtendimento().getClienteTipoDeDocFiscal(), 
        		atendimentoAggregate.getAtendimento().getClienteDocFiscal(), 
        		atendimentoAggregate.getAtendimento().getClienteNome(), 
        		atendimentoAggregate.getAtendimento().getClienteTelefone(), 
        		atendimentoAggregate.getAtendimento().getClienteWhatsapp(), 
        		atendimentoAggregate.getAtendimento().getVeiculoPlaca(), 
        		atendimentoAggregate.getAtendimento().getTipoOcorrencia(), 
        		servicoId, 
        		atendimentoAggregate.getAtendimento().getServicoNome(), 
        		atendimentoAggregate.getAtendimento().getStatus(), 
        		atendimentoAggregate.getAtendimento().getDataHoraSolicitado(),
        		atendimentoAggregate.getAtendimento().getDataHoraConfirmado(),
        		atendimentoAggregate.getAtendimento().getDataHoraAjustado(),
        		atendimentoAggregate.getAtendimento().getDataHoraFinalizado(),
        		atendimentoAggregate.getAtendimento().getDataHoraCancelado(),
        		atendimentoAggregate.getAtendimento().getDescricao(), 
        		atendimentoAggregate.getAtendimento().getOrigem(), 
        		atendimentoAggregate.getAtendimento().getDestino(), 
        		atendimentoAggregate.getAtendimento().getBase(), 
        		items);
        this.sendAtendimentoMessage(atendimentoDTO);
    }

    @SneakyThrows
    private void sendAtendimentoMessage(AtendimentoDTO atendimentoDTO) {
        this.rabbitTemplate.convertAndSend(this.exchange, this.routingKey,
                new ObjectMapper().writeValueAsString(atendimentoDTO));
        log.info("With routingKey {}, sent Order message {}, to exchange {}", this.routingKey, atendimentoDTO,
                this.exchange);
    }

    @Nonnull
    @Override
    public String getAggregateType() {
        return AggregateType.ATENDIMENTO.toString();
    }
}
