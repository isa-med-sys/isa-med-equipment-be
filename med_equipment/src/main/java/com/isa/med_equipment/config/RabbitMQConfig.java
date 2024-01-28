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

    @Value("${rabbitmq.hosp-producer.queue.name}")
    private String hospQueue;
    @Value("${rabbitmq.hosp-producer.exchange.name}")
    private String hospExchange;
    @Value("${rabbitmq.hosp-producer.routing.key.name}")
    private String hospRoutingKey;

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
    public Queue hospitalQueue (){
        return new Queue(hospQueue);
    }

    @Bean
    public TopicExchange hospitalExchange(){
        return new TopicExchange(hospExchange);
    }

    @Bean
    public Binding hospitalBinding(){
        return BindingBuilder.bind(hospitalQueue()).to(hospitalExchange()).with(hospRoutingKey);
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