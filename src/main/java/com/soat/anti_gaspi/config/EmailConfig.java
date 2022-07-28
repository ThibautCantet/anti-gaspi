package com.soat.anti_gaspi.config;

import com.soat.anti_gaspi.service.EmailService;
import com.soat.anti_gaspi.service.JavaMailService;
import com.soat.anti_gaspi.service.SendGridService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class EmailConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailConfig.class);


    @Bean(name = "emailService")
    EmailService EmailFactory(
            @Value("${senGrid.enabled}") boolean sendGridEnabled,
            JavaMailService javaMailService,
            SendGridService sendGridService
            ) {
        if (sendGridEnabled) {
            LOGGER.info("send grid is chosen.");
            return sendGridService;
        }
        LOGGER.info("JavaMail is chosen.");
        return javaMailService;
    }
}
