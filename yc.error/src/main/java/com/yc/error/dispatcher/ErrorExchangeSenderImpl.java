package com.yc.error.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ErrorExchangeSenderImpl implements ErrorExchangeSender
{
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${yc.eh.exchange.error.name}")
    private String errorExchangeName;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void registerError(String queueName, String message)
    {
        logger.info(" > registerError(): message: "+message);

        this.rabbitTemplate.convertAndSend(queueName, "", message);
    }

    @Override
    public void registerPlatformError(String message)
    {
        logger.info(" > registerPlatformError(): message: "+message);

        this.rabbitTemplate.convertAndSend(this.errorExchangeName, "up", message);
    }

    @Override
    public void registerApplicationError(String message)
    {
        logger.info(" > registerApplicationError(): message: "+message);

        this.rabbitTemplate.convertAndSend(this.errorExchangeName, "ua", message);
    }

    @Override
    public void notifyQueryModel(String exchange, String keyRouting, String message)
    {
        logger.info(" > notifyQueryModel(): message: "+message);

        this.rabbitTemplate.convertAndSend(exchange, keyRouting, message);
    }
}
