package com.example.G4_DailyReport.config;

import org.modelmapper.Condition;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper createModelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        Condition<Object, Object> notNull = ctx -> ctx.getSource() != null;
        modelMapper.getConfiguration().setPropertyCondition(notNull);
        return modelMapper;
    }
}
