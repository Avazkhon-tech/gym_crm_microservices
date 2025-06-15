package com.epam.messaging;

import com.epam.dto.training.TrainerWorkloadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageSender {

    private final CustomJmsSender jmsTemplate;

    public void sendTrainerWorkload(TrainerWorkloadDto trainerWorkloadDto) {
        jmsTemplate.send("trainer.workload.queue", trainerWorkloadDto);
    }
}
