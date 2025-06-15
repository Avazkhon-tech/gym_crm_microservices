package com.epam.feign.fallback;

import com.epam.dto.training.TrainerWorkloadDto;
import com.epam.feign.TrainerWorkloadClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TrainerWorkloadFallback implements TrainerWorkloadClient {

    @Override
    public void updateTrainerWorkload(TrainerWorkloadDto trainerWorkloadDto, String token, String transactionId) {
        log.warn("Trainer workload service is down. Update trainer workload is not performed: {}", trainerWorkloadDto);
    }
}