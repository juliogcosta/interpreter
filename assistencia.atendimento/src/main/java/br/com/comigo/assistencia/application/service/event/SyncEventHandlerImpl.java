package br.com.comigo.assistencia.application.service.event;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yc.core.cqrs.application.service.event.SyncEventHandler;
import com.yc.core.cqrs.domain.Aggregate;
import com.yc.core.cqrs.domain.event.EventWithId;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Component
public class SyncEventHandlerImpl implements SyncEventHandler {

	/**
	 * Aqui é o local para invocar o persistence-c e passar o comando UPDATE.
	 * 
	 */
	
	@Override
	public void handleEvents(List<EventWithId> events, Aggregate aggregate) {
		this.updateAggregateProjection(aggregate);
	}
	
	private void updateAggregateProjection(Aggregate aggregate) {
		log.info("\n > aggregate: ", aggregate);
	}
}
