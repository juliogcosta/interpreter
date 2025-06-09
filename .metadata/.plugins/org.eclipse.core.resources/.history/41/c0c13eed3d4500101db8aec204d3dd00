package com.yc.collector.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CollectorExchangeSenderImpl implements CollectorExchangeSender
{
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${yc.eh.exchange.consumption.name}")
    private String consumptioExchangeName;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void registerApplicationConsumption(String message)
    {
        this.rabbitTemplate.convertAndSend(this.consumptioExchangeName, "ua", message);
    }
}
