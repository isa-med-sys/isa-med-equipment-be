package com.isa.med_equipment.rabbitMQ;

import com.isa.med_equipment.dto.LocationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumerLocation {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumerLocation.class);

    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void consume(LocationDto location){
        LOGGER.info(String.format("Received -> %s", location.toString()));
    }
}
