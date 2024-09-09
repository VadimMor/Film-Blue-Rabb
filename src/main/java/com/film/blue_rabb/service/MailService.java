package com.film.blue_rabb.service;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

@Service
public interface MailService {
    void sendActiveCode(String email, String code) throws MessagingException;
}
