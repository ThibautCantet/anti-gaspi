package com.soat.anti_gaspi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

@Configuration
@Profile("AcceptanceTest")
public class ClockConfig {
    @Bean
    public Clock clock() {
        return Clock.fixed(LocalDate.parse("2023-04-04").atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.of("UTC"));
    }

}
