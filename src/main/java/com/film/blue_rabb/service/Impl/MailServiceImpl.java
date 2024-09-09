package com.film.blue_rabb.service.Impl;

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
    public void sendActiveCode(String email, String code) throws MessagingException {
        log.trace("MailServiceImpl.sendActiveCode - email {}, code {}", email, code);

        String subject = "Активация аккаунта";
        Context context = new Context();
        context.setVariable("code", code);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setFrom(username);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject(subject);
        String htmlContent = templateEngine.process("ActivationCode", context);
        mimeMessageHelper.setText(htmlContent, true);

        try {
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.trace("ERROR - MailServiceImpl.sendActiveCode");
            throw e;
        }
    }
}
