package com.epam.feign.fallback;

import com.epam.dto.tranining.TrainerWorkloadDto;
import com.epam.feign.TrainerWorkloadClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TrainerWorkloadFallback implements TrainerWorkloadClient {

    @Override
    public void updateTrainerWorkload(TrainerWorkloadDto trainerWorkloadDto, String token, String transactionId) {
        log.debug("Circuit breaker triggered! Fallback logic executed.");
    }
}
