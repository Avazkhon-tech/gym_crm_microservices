package com.epam.trainerworkloadservice.messaging;

import com.epam.trainerworkloadservice.dto.TrainerWorkloadDto;
import com.epam.trainerworkloadservice.utility.TransactionId;
import com.epam.trainerworkloadservice.utility.TransactionIdExtractorJms;
import jakarta.jms.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DLQListener {

    @JmsListener(destination = "ActiveMQ.DLQ")
    public void handleDLQ(@Payload TrainerWorkloadDto trainerWorkloadDto, Message message) {
        TransactionIdExtractorJms.extractFromMessage(message);
        log.warn("[{}] Received message in DLQ: {}", TransactionId.getTransaction(), trainerWorkloadDto);
        // we can save the info to the database and notify the admin about the situation
    }
}