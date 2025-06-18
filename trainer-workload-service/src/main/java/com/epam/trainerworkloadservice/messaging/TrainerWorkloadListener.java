package com.epam.trainerworkloadservice.messaging;

import com.epam.trainerworkloadservice.dto.TrainerWorkloadDto;
import com.epam.trainerworkloadservice.exception.TrainerAlreadyBusyException;
import com.epam.trainerworkloadservice.service.TrainerWorkloadService;
import com.epam.trainerworkloadservice.utility.TransactionId;
import com.epam.trainerworkloadservice.utility.TransactionIdExtractorJms;
import jakarta.jms.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TrainerWorkloadListener {

    private final TrainerWorkloadService trainerWorkloadService;
    private final JmsTemplate jmsTemplate;

    public TrainerWorkloadListener(TrainerWorkloadService trainerWorkloadService, JmsTemplate jmsTemplate) {
        this.trainerWorkloadService = trainerWorkloadService;
        this.jmsTemplate = jmsTemplate;
    }

    @JmsListener(destination = "trainer.workload.queue")
    public void receiveTrainerWorkload(@Payload TrainerWorkloadDto trainerWorkloadDto, Message message) {
        TransactionIdExtractorJms.extractFromMessage(message);
        try {
            trainerWorkloadService.updateTrainerWorkload(trainerWorkloadDto);
        } catch (TrainerAlreadyBusyException ex) {
            log.warn("[{}] Trainer is busy: {}", TransactionId.getTransaction(), trainerWorkloadDto);
            jmsTemplate.convertAndSend("ActiveMQ.DLQ", trainerWorkloadDto, msg -> {
                msg.setStringProperty("TransactionId", TransactionId.getTransaction());
                return msg;
            });
        }
    }
}
