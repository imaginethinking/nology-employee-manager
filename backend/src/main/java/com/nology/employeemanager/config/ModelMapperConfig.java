package com.nology.employeemanager.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setPreferNestedProperties(false)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        mapper.addConverter(ctx -> {
            String source = ctx.getSource();

            if (source == null) {
                return null;
            }

            String trimmed = source.trim();

            return trimmed.isBlank() ? null : trimmed;
        }, String.class, String.class);

        return mapper;
    }
}
