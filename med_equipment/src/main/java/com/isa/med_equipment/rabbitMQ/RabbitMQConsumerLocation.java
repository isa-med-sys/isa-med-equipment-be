package com.isa.med_equipment.rabbitMQ;

import com.isa.med_equipment.dto.LocationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RabbitMQConsumerLocation {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumerLocation.class);
    private final SimpMessagingTemplate simpMessagingTemplate;
    public RabbitMQConsumerLocation(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }
    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void consume(LocationDto location){
        LOGGER.info(String.format("Received -> %s", location.toString()));
        Map<String, Object> locationMap = new HashMap<>();
        locationMap.put("latitude", location.getLatitude());
        locationMap.put("longitude", location.getLongitude());
        //user -> company
        this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + location.getUserId(), locationMap);
    }
}
