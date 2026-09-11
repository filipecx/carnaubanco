package com.carnaubanco.walletservice.consumer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.carnaubanco.walletservice.config.RabbitMQConfig;
import com.carnaubanco.walletservice.dtos.TransactionCompletedEvent;

@Component 
public class TransactionNotificationConsumer {
    private static final Logger log = LoggerFactory.getLogger(TransactionNotificationConsumer.class);

    @RabbitListener (queues = RabbitMQConfig.QUEUE_NAME)
    public void consumeTransactionCompleted(TransactionCompletedEvent event) {
        log.info("==================================");
        log.info("[EVENTO ASSÍNCRONO RECEBIDO DO RABBITMQ]");
        log.info("==================================");

    }
    
}
