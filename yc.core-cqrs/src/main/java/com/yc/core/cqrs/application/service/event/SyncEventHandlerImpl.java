package com.yc.core.cqrs.application.service.event;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yc.core.cqrs.domain.Aggregate;
import com.yc.core.cqrs.domain.event.EventWithId;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Component
@RequiredArgsConstructor
@Slf4j
public class SyncEventHandlerImpl implements SyncEventHandler {

	@Override
	public void handleEvents(List<EventWithId> events, Aggregate aggregate) {
		this.updateAggregateProjection(aggregate);
	}
	
	private void updateAggregateProjection(Aggregate aggregate) {
		/**
		 * Aqui a oportunidade para invocação de persistence-c, a fim de atualizar
		 * a projecao do agregado.
		 * 
		 * precisa estar nesse ponto com o tenant.
		 * 
		 */
		log.info("\n > aggregate: ", aggregate);
	}

}
