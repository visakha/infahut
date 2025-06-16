package com.infahud.infahut.web.config;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        // ObjectMapper mapper = new ObjectMapper();
        // mapper.disable(MapperFeature.REQUIRE_HANDLERS_FOR_JAVA8_TIMES);
        JsonMapper mapper = JsonMapper.builder()
                .disable(MapperFeature.REQUIRE_HANDLERS_FOR_JAVA8_TIMES)
                .build();        
        //JsonMapper.builder().disable(MapperFeature.REQUIRE_HANDLERS_FOR_JAVA8_TIMES)
        return mapper;
    }
}