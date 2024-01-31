package com.isa.med_equipment.rabbitMQ;

import com.isa.med_equipment.dto.ContractDto;
import com.isa.med_equipment.dto.ContractGetDto;
import com.isa.med_equipment.service.ContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQHospitalConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQHospitalConsumer.class);
    private final RabbitMQHospitalProducer mqHospitalProducer;
    private final ContractService contractService;


    public RabbitMQHospitalConsumer(RabbitMQHospitalProducer mqHospitalProducer, ContractService contractService) {
        this.mqHospitalProducer = mqHospitalProducer;
        this.contractService = contractService;
    }

    @RabbitListener(queues = {"${rabbitmq.hosp-consumer.handle.queue.name}"})
    public void handleContract(ContractDto message) {
        try {
            LOGGER.info(String.format("Received -> %s", message.toString()));
            if (message.getDelete()) {
                contractService.delete(message.getId());
            } else {
                contractService.handleReceived(message);
            }
        } catch (Exception e) {
            LOGGER.error("Error processing contract message: " + message.toString(), e);
        }
    }

    @RabbitListener(queues = {"${rabbitmq.hosp-consumer.get.queue.name}"})
    public void getContract(ContractGetDto message) {
        try {
            LOGGER.info(String.format("Received -> %s", message != null ? message.toString() : "null message"));
            if (message != null && message.getUserId() != null) {
                ContractDto contract = contractService.findActiveByUser(message.getUserId());
                if (contract != null) {
                    mqHospitalProducer.sendContract(contract);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error processing getContract message: " + (message != null ? message.toString() : "null message"), e);
        }
    }
}