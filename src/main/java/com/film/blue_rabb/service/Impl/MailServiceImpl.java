package com.film.blue_rabb.service.Impl;

import com.film.blue_rabb.exception.custom.MessageMailException;
import com.film.blue_rabb.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;
    private final @Autowired TemplateEngine templateEngine;
    @Value(value = "${application.authorization.path}")
    private String auth;
    @Value(value = "${spring.mail.host}")
    private String host;
    @Value(value = "${spring.mail.username}")
    private String username;
    @Value(value = "${spring.mail.password}")
    private String password;
    @Value(value = "${spring.mail.port}")
    private int port;
    @Value(value = "${spring.mail.protocol}")
    private String protocol;
    @Override
    public void sendActiveCode(String email, String code) throws MessagingException, MessageMailException {
        log.trace("MailServiceImpl.sendActiveCode - email {}, code {}", email, code);
        // Установка темы письма
        String subject = "Активация аккаунта";
        // Создаем объект Context для Thymeleaf шаблонизатора и добавляем переменную "code"
        Context context = new Context();
        context.setVariable("code", code);
        context.setVariable("email", email);
        // Создаем MIME сообщение с помощью JavaMailSender
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // Используем MimeMessageHelper для настройки сообщения
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setFrom(username);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject(subject);
        // Генерируем HTML контент письма на основе шаблона "ActivationCode" и переданного контекста
        String htmlContent = templateEngine.process("ActivationCode", context);
        mimeMessageHelper.setText(htmlContent, true);
        try {
            // Отправляем письмо
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("ERROR - MailServiceImpl.sendActiveCode");
            throw new MessageMailException();
        }
    }
}
