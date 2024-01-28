package com.isa.med_equipment.rabbitMQ;

import com.isa.med_equipment.dto.StartDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducerLocation {

    @Value("${rabbitmq.starting.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.starting.routing.key.name}")
    private String routingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQProducerLocation.class);
    private final RabbitTemplate rabbitTemplate;

    public RabbitMQProducerLocation(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // TODO set startDto.userId 
    public void sendMessage(StartDto startDto) {
        LOGGER.info("Message -> {}", startDto);
        rabbitTemplate.convertAndSend(exchange, routingKey, startDto);
    }
}