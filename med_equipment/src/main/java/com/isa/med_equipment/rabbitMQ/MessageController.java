package com.isa.med_equipment.rabbitMQ;

import com.isa.med_equipment.dto.StartDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mq")
public class MessageController {

    private RabbitMQProducerLocation producer;

    public MessageController(RabbitMQProducerLocation producer) {
        this.producer = producer;
    }

    @PostMapping("/start")
    public ResponseEntity<String> startSimulation(@RequestBody StartDto start){
        producer.sendMessage(start);
        return ResponseEntity.ok("Started!");
    }
}