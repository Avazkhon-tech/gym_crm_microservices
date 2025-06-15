package com.epam.feign;

import com.epam.dto.training.TrainerWorkloadDto;
import com.epam.feign.fallback.TrainerWorkloadFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "trainer-workload-service", fallback = TrainerWorkloadFallback.class)
public interface TrainerWorkloadClient {

    @PostMapping("/api/v1/trainer-workload")
    void updateTrainerWorkload(@RequestBody TrainerWorkloadDto trainerWorkloadDto,
                               @RequestHeader("Authorization") String token,
                               @RequestHeader("TransactionId") String transactionId);
}
