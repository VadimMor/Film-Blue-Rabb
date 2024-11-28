package com.film.blue_rabb.service;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

@Service
public interface MailService {
    /**
     * Отправка кода активации новому пользователю
     * @param email почта нового пользователя
     * @param code код активации
     */
    void sendActiveCode(String email, String code) throws MessagingException;
}
