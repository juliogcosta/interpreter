package br.com.comigo.assistencia.adapter.inbound.queue;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageConsumer {

  @Value("${spring.rabbitmq.assistencia-catalogo.servico.events.view-update.queue}")
  private String queue;

  @RabbitListener(queues = "${spring.rabbitmq.assistencia-catalogo.servico.events.view-update.queue}")
  public void receiveMessage(String message) {
    log.info("Received message from queue {}: {}", this.queue, message);
    // Adicionar sua lógica de processamento

    // Exemplo de tratamento de erro (simulando falha)
    if (message.contains("error")) {
      throw new RuntimeException("Simulated processing error");
    }
  }
}