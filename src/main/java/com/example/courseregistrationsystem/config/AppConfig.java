package com.example.courseregistrationsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.courseregistrationsystem.strategy.GradingStrategy;
import com.example.courseregistrationsystem.strategy.StandardGradingStrategy;
import com.example.courseregistrationsystem.observer.EmailNotificationObserver;

@Configuration
public class AppConfig {
    
    @Bean
    public GradingStrategy gradingStrategy() {
        return new StandardGradingStrategy();
    }
    
    @Bean
    public EmailNotificationObserver emailNotificationObserver() {
        return new EmailNotificationObserver();
    }
} 