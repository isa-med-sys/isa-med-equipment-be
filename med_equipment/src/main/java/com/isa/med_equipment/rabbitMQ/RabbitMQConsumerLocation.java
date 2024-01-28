package com.isa.med_equipment.rabbitMQ;

import com.isa.med_equipment.dto.DeliveryStartDto;
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
    private final RabbitMQHospitalProducer mqHospitalProducer;

    public RabbitMQConsumerLocation(SimpMessagingTemplate simpMessagingTemplate, RabbitMQHospitalProducer mqHospitalProducer) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.mqHospitalProducer = mqHospitalProducer;
    }

    @RabbitListener(queues = {"${rabbitmq.locations.queue.name}"})
    public void consume(LocationDto location){
        LOGGER.info(String.format("Received -> %s", location.toString()));
        Map<String, Object> locationMap = new HashMap<>();
        locationMap.put("latitude", location.getLatitude());
        locationMap.put("longitude", location.getLongitude());
        this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + location.getCompanyId(), locationMap);
    }

    @RabbitListener(queues = "${rabbitmq.simulation.queue.name}")
    public void consumeSimulation(DeliveryStartDto deliveryStart) {
        LOGGER.info(String.format("Received Simulation Start -> %s", deliveryStart.toString()));
        mqHospitalProducer.notifyOfDeliveryStart(deliveryStart);
    }
}