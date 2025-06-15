package com.epam.messaging;

import com.epam.utility.TransactionId;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomJmsSender {

    private final JmsTemplate jmsTemplate;

    public void send(String destination, Object payload) {
        jmsTemplate.convertAndSend(destination, payload, message -> {
            message.setStringProperty("TransactionId", TransactionId.getTransaction());
            return message;
        });
    }
}
