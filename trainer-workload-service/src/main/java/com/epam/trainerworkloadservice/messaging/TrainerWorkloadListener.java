package com.epam.trainerworkloadservice.messaging;

import com.epam.trainerworkloadservice.dto.TrainerWorkloadDto;
import com.epam.trainerworkloadservice.service.TrainerWorkloadService;
import com.epam.trainerworkloadservice.utility.TransactionIdExtractorJms;
import jakarta.jms.Message;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class TrainerWorkloadListener {

    private final TrainerWorkloadService trainerWorkloadService;

    public TrainerWorkloadListener(TrainerWorkloadService trainerWorkloadService) {
        this.trainerWorkloadService = trainerWorkloadService;
    }

    @JmsListener(destination = "trainer.workload.queue")
    public void receiveTrainerWorkload(@Payload TrainerWorkloadDto trainerWorkloadDto, Message message) {
        TransactionIdExtractorJms.extractFromMessage(message);
        trainerWorkloadService.updateTrainerWorkload(trainerWorkloadDto);
    }
}
