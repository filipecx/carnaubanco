package com.carnaubanco.walletservice.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration 
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "wallet.events.exchange";
    public static final String QUEUE_NAME = "wallet.transaction.notification.queue";
    public static final String ROUTING_KEY = "wallet.transaction.completed";

    //Cria a topic exchange
    @Bean 
    public TopicExchange walletEventsExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    //Cria a fila que não some se o rabbitmq cair
    @Bean 
    public Queue transactionNotificationQueue() {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    //Cria o binding (liga a fila na exchange usando a routing key)
    @Bean 
    public Binding binding(Queue transactionNotificationQueue, TopicExchange walletEventsTopicExchange) {
        return BindingBuilder
        .bind(transactionNotificationQueue)
        .to(walletEventsTopicExchange)
        .with(ROUTING_KEY);
    }

    
    //Conversor para mandar as mensagens como json
    @Bean 
    public Jackson2JsonMessageConverter messageConverter() {
        
    
        return new Jackson2JsonMessageConverter();
    }

    @Bean 
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean 
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }
}
