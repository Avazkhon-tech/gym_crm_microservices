package com.epam.config;

import com.epam.security.JwtProvider;
import com.epam.utility.TransactionId;
import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Profile("!test")
@Configuration
@RequiredArgsConstructor
public class TrainerWorkloadFeignConfig {

    private final JwtProvider jwtProvider;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer " + jwtProvider.generateToken("main-service"));

            requestTemplate.header("TransactionId", TransactionId.getTransaction());
        };
    }
}
