package com.film.blue_rabb.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncoder {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * Функция кодирования пароля
     * @param password пароль
     * @return возвращает закодированный пароль
     */
    public static String getEncryptedPassword(String password) {
        return encoder.encode(password);
    }

    /**
     * Функция проверки пароля с закодированной частью
     * @param rawPassword пароль
     * @param encodePassword закодированный пароль
     * @return возвращает результат проверки
     */
    public static boolean arePasswordsEquals(String rawPassword, String encodePassword){
        return encoder.matches(rawPassword, encodePassword);
    }

    /**
     * Функция проверки пароля с закодированной частью
     * @return возвращает метод BCryptPasswordEncoder
     */
    public BCryptPasswordEncoder getEncoder(){
        return encoder;
    }
}
