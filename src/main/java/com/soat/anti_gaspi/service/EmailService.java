package com.soat.anti_gaspi.service;

import com.soat.anti_gaspi.model.NotificationException;

import java.io.IOException;

public interface EmailService {
    void sendEmail(String subject, String beneficiaire, String body) ;
}
