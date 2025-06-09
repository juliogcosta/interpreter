package br.com.comigo.assistencia.application.service.event;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.comigo.assistencia.adapter.outbount.projection.JpaServicoProjection;
import br.com.comigo.assistencia.adapter.outbount.projection.respository.JpaServicoProjectionRepository;
import br.com.comigo.assistencia.domain.aggregate.AggregateType;
import br.com.comigo.assistencia.domain.aggregate.ServicoAggregate;
import br.com.comigo.core.application.service.event.handler.SyncEventHandler;
import br.com.comigo.core.domain.Aggregate;
import br.com.comigo.core.domain.event.Event;
import br.com.comigo.core.domain.event.EventWithId;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Component
@RequiredArgsConstructor
@Slf4j
public class ServicoProjectionUpdater implements SyncEventHandler {

    private final JpaServicoProjectionRepository jpaServicoProjectionRepository;

    @Override
    public void handleEvents(List<EventWithId<Event>> events, Aggregate aggregate) {
        log.debug("Updating read model for {}", aggregate);
        try {
			this.updateServicoProjection((ServicoAggregate) aggregate);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
    }

    private void updateServicoProjection(ServicoAggregate aggregate) throws JsonProcessingException {
    	log.info(" > aggregate: {}", aggregate);
    	log.info(" > aggregate as json: {}", new ObjectMapper().writeValueAsString(aggregate));
    	JpaServicoProjection jpaServicoProjection = jpaServicoProjectionRepository.findById(aggregate.getAggregateId())
    		.orElse(new JpaServicoProjection());
        if (jpaServicoProjection.getId() == null) {
        	jpaServicoProjection.setId(aggregate.getServico().getId());
        }
    	jpaServicoProjection.setNome(aggregate.getServico().getNome());
        jpaServicoProjection.setDescricao(aggregate.getServico().getDescricao());
        jpaServicoProjection.setStatus(aggregate.getServico().getStatus());
        jpaServicoProjection.setCertificacaoIso(aggregate.getServico().getCertificacaoIso());
        jpaServicoProjection.setDataHoraDisponibilizado(aggregate.getServico().getDataHoraDisponibilizado());
        jpaServicoProjection.setDataHoraIndisponibilizado(aggregate.getServico().getDataHoraIndisponibilizado());
        jpaServicoProjection.setDataHoraAjustado(aggregate.getServico().getDataHoraAjustado());
        jpaServicoProjection.setDataHoraCancelado(aggregate.getServico().getDataHoraCancelado());
        jpaServicoProjection.setVersion(aggregate.getVersion());
        
        this.jpaServicoProjectionRepository.save(jpaServicoProjection);
    }

    @Nonnull
    @Override
    public String getAggregateType() {
        return AggregateType.SERVICO.toString();
    }
}
