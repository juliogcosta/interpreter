package com.yc.core.cqrs.application.service.event.processor;

/**
 * Essa classe implementa um Bean a ser gerenciado pelo spring e que será
 * invocado por meio de processNewEvents(). A periodicidade da invocação do
 * método é dado por propriedade de ambiente: event-sourcing.subscriptions
 * 
 */

//@Component
//@ConditionalOnProperty(name = "event-sourcing.subscriptions", havingValue = "polling")
//@RequiredArgsConstructor
//@Slf4j
public class ScheduledEventSubscriptionProcessor {

	/**
	 * batchSize: informa o tamanho do batch de eventos a serem processados. Isso
	 * evita que se torne um problema de eficiência grave no sistema se um handler
	 * novo for adicionado (caso seja obrigado) a processar todos os eventos de um
	 * agregado, sendo muitos.
	 * 
	 * /
	@Value("${event-sourcing.polling-subscriptions.batch-size}")
	private int batchSize;

	private final AsyncEventHandler eventHandlers;
	private final EventSubscriptionProcessor eventSubscriptionProcessor;

	@Scheduled(fixedDelayString = "${event-sourcing.polling-subscriptions.polling-interval}", initialDelayString = "${event-sourcing.polling-subscriptions.polling-initial-delay}")
	public void processNewEvents() {
		this.eventHandlers.forEach(this::processNewEvents);
	}

	private void processNewEvents(AsyncEventHandler eventHandler) {
		try {
			this.eventSubscriptionProcessor.processNewEvents(this.schemaName, null, eventHandler, this.batchSize);
		} catch (Exception e) {
			log.warn("Failed to handle new events for subscription %s".formatted(eventHandler.getSubscriptionName()), e);
		}
	}*/
}
