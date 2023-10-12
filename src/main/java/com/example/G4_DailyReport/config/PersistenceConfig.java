package com.example.G4_DailyReport.config;

import com.example.G4_DailyReport.spring.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaAuditing
public class PersistenceConfig {
    @Bean
    public AuditorAware<String> auditorProvider() {
        // our implementation of AuditorAware
        return new AuditorAwareImpl();
    }
}
