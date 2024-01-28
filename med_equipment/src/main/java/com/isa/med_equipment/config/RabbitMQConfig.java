package com.isa.med_equipment.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.starting.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.starting.queue.name}")
    private String queue;
    @Value("${rabbitmq.starting.routing.key.name}")
    private String routingKey;

    @Value("${rabbitmq.hosp-producer.contract.queue.name}")
    private String hospQueue1;
    @Value("${rabbitmq.hosp-producer.contract.exchange.name}")
    private String hospExchange1;
    @Value("${rabbitmq.hosp-producer.contract.routing.key.name}")
    private String hospRoutingKey1;

    @Value("${rabbitmq.hosp-producer.notif.queue.name}")
    private String hospQueue2;
    @Value("${rabbitmq.hosp-producer.notif.exchange.name}")
    private String hospExchange2;
    @Value("${rabbitmq.hosp-producer.notif.routing.key.name}")
    private String hospRoutingKey2;

    @Bean
    public Queue queue() {
        return new Queue(queue);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(routingKey);
    }

    @Bean
    public Queue hospitalQueue1(){
        return new Queue(hospQueue1);
    }

    @Bean
    public TopicExchange hospitalExchange1(){
        return new TopicExchange(hospExchange1);
    }

    @Bean
    public Binding hospitalBinding1(){
        return BindingBuilder.bind(hospitalQueue1()).to(hospitalExchange1()).with(hospRoutingKey1);
    }

    @Bean
    public Queue hospitalQueue2(){
        return new Queue(hospQueue2);
    }

    @Bean
    public TopicExchange hospitalExchange2(){
        return new TopicExchange(hospExchange2);
    }

    @Bean
    public Binding hospitalBinding2(){
        return BindingBuilder.bind(hospitalQueue2()).to(hospitalExchange2()).with(hospRoutingKey2);
    }

    @Bean
    public MessageConverter converter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory, MessageConverter converter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter);
        return rabbitTemplate;
    }
}