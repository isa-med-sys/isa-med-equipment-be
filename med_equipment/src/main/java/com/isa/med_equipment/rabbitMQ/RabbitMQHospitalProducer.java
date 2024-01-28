package com.isa.med_equipment.rabbitMQ;

import com.isa.med_equipment.dto.ContractDto;
import com.isa.med_equipment.dto.ContractNotificationDto;
import com.isa.med_equipment.dto.DeliveryStartDto;
import com.isa.med_equipment.service.ContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RabbitMQHospitalProducer {

    @Value("${rabbitmq.hosp-producer.notif.exchange.name}")
    private String notifExchange;

    @Value("${rabbitmq.hosp-producer.notif.routing.key.name}")
    private String notifRoutingKey;

    @Value("${rabbitmq.hosp-producer.contract.exchange.name}")
    private String contractExchange;

    @Value("${rabbitmq.hosp-producer.contract.routing.key.name}")
    private String contractRoutingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQHospitalProducer.class);
    private final RabbitTemplate rabbitTemplate;
    private final ContractService contractService;

    @Autowired
    public RabbitMQHospitalProducer(ContractService contractService, RabbitTemplate rabbitTemplate) {
        this.contractService = contractService;
        this.rabbitTemplate = rabbitTemplate;
    }

    // TODO uncomment
    //  @Scheduled(cron = "0 0 7 * * ?")
    @Scheduled(cron = "0 * * * * ?")
    public void checkUpcomingDeliveries() {
        List<ContractDto> upcomingContracts = contractService.findAllScheduledForDelivery();
        for(ContractDto contract : upcomingContracts) {
            if(!contractService.canBeDelivered(contract.getId())) {
                ContractNotificationDto message = new ContractNotificationDto(
                        contract.getUserId(),
                        "Delivery cannot be made. Equipment is out of stock." );
                sendNotification(message);
            }
        }
    }

    // TODO test
    public void notifyOfDeliveryStart(DeliveryStartDto deliveryStart) {
        ContractNotificationDto message = new ContractNotificationDto(
                deliveryStart.getUserId(),
                String.format("Your delivery has started. Estimated delivery time: %d minutes",
                        deliveryStart.getTotalDurationInMinutes()));
        sendNotification(message);
    }

    public void sendContract(ContractDto message) {
        LOGGER.info(String.format("Message -> %s", message));
        rabbitTemplate.convertAndSend(contractExchange, contractRoutingKey, message);
    }

    private void sendNotification(ContractNotificationDto message) {
        LOGGER.info(String.format("Message -> %s", message));
        rabbitTemplate.convertAndSend(notifExchange, notifRoutingKey, message);
    }
}
