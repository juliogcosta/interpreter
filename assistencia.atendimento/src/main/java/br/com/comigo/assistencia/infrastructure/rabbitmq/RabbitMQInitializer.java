package br.com.comigo.assistencia.infrastructure.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Slf4j
@Configuration
@EnableRetry
public class RabbitMQInitializer implements HealthIndicator {

  @Value("${spring.rabbitmq.host}")
  private String host;

  @Value("${spring.rabbitmq.port}")
  private int port;

  @Value("${spring.rabbitmq.username}")
  private String username;

  @Value("${spring.rabbitmq.password}")
  private String password;

  @Value("${spring.rabbitmq.assistencia-atendimento.atendimento.exchange}")
  private String exchange;

  @Value("${spring.rabbitmq.assistencia-atendimento.atendimento.events.view-update.queue}")
  private String queue;

  @Value("${spring.rabbitmq.assistencia-atendimento.atendimento.events.view-update.queue-dlq}")
  private String queueDlq;

  @Value("${spring.rabbitmq.assistencia-atendimento.atendimento.events.view-update.routing-key}")
  private String routingKey;

  private boolean infrastructureReady = false;

  @jakarta.annotation.PostConstruct
  @Async
  public void initialize() {
    try {
      initializeRabbitMQInfrastructureWithRetry();
      infrastructureReady = true;
    } catch (Exception e) {
      log.error("Falha crítica ao inicializar infraestrutura RabbitMQ", e);
      infrastructureReady = false;
    }
  }

  @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 2000, multiplier = 2), include = { IOException.class,
      TimeoutException.class })
  private void initializeRabbitMQInfrastructureWithRetry() throws IOException, TimeoutException {
    log.info("Tentando inicializar infraestrutura RabbitMQ...");
    CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
    connectionFactory.setPort(port);
    connectionFactory.setUsername(username);
    connectionFactory.setPassword(password);

    try (Connection connection = connectionFactory.createConnection();
        Channel channel = connection.createChannel(false)) {

      createExchangeWithVerification(channel);
      createQueueWithVerification(channel, queue, true);
      createQueueWithVerification(channel, queueDlq, false);
      createBindings(channel);

      log.info("Infraestrutura RabbitMQ inicializada com sucesso");
    }
  }

  private void createExchangeWithVerification(Channel channel) throws IOException {
    try {
      channel.exchangeDeclarePassive(exchange);
      log.info("Exchange '{}' já existe", exchange);
    } catch (IOException e) {
      channel.exchangeDeclare(exchange, BuiltinExchangeType.DIRECT, true);
      log.info("Exchange '{}' criado com sucesso", exchange);
    }
  }

  private void createQueueWithVerification(Channel channel, String queueName, boolean withDLX) throws IOException {
    try {
      channel.queueDeclarePassive(queueName);
      log.info("Fila '{}' já existe", queueName);
    } catch (IOException e) {
      Map<String, Object> args = withDLX ? Map.of(
          "x-dead-letter-exchange", exchange,
          "x-dead-letter-routing-key", queueDlq) : null;

      channel.queueDeclare(queueName, true, false, false, args);
      log.info("Fila '{}' criada com sucesso", queueName);
    }
  }

  private void createBindings(Channel channel) throws IOException {
    try {
      channel.queueBind(queue, exchange, routingKey);
      log.info("Binding criado: '{}' -> '{}' com chave '{}'", queue, exchange, routingKey);

      channel.queueBind(queueDlq, exchange, queueDlq);
      log.info("Binding DLQ criado: '{}' -> '{}' com chave '{}'", queueDlq, exchange, queueDlq);
    } catch (IOException e) {
      log.error("Falha ao criar bindings", e);
      throw e;
    }
  }

  @Override
  public Health health() {
    if (!infrastructureReady) {
      return Health.down()
          .withDetail("message", "Infraestrutura RabbitMQ não inicializada")
          .build();
    }

    CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
    connectionFactory.setPort(port);
    connectionFactory.setUsername(username);
    connectionFactory.setPassword(password);

    try (Connection connection = connectionFactory.createConnection();
        Channel channel = connection.createChannel(false)) {

      // Verifica se o exchange está acessível
      channel.exchangeDeclarePassive(exchange);

      // Verifica se as filas principais estão acessíveis
      channel.queueDeclarePassive(queue);
      channel.queueDeclarePassive(queueDlq);

      return Health.up()
          .withDetail("exchange", exchange)
          .withDetail("queue", queue)
          .withDetail("dlq", queueDlq)
          .build();

    } catch (Exception e) {
      infrastructureReady = false;
      return Health.down(e)
          .withDetail("exchange", exchange)
          .withDetail("queue", queue)
          .withDetail("error", e.getMessage())
          .build();
    }
  }

  public boolean isInfrastructureReady() {
    return infrastructureReady;
  }
}