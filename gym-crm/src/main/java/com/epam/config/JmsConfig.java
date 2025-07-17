package com.epam.config;

import com.epam.dto.training.TrainerWorkloadDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.HashMap;
import java.util.Map;

@Profile("!test")
@Configuration
public class JmsConfig {

    @Bean
    public MessageConverter jacksonJmsMessageConverter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper);
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        setTypeMappingsToMessageConverter(converter);
        return converter;
    }

    private void setTypeMappingsToMessageConverter(MappingJackson2MessageConverter converter) {
        Map<String, Class<?>> mappings = new HashMap<>();
        mappings.put("trainerWorkload", TrainerWorkloadDto.class);
        converter.setTypeIdMappings(mappings);
    }
}
