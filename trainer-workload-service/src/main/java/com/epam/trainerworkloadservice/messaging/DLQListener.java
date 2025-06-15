package com.epam.trainerworkloadservice.messaging;

import com.epam.trainerworkloadservice.dto.TrainerWorkloadDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DLQListener {

    @JmsListener(destination = "ActiveMQ.DLQ")
    public void handleDLQ(TrainerWorkloadDto trainerWorkloadDto) {
        log.warn("Received message in DLQ: {}", trainerWorkloadDto);
        // we can save the info to the database and notify the admin about the situation
    }
}