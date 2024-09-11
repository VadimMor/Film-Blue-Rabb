package com.film.blue_rabb.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class ActiveCode {
    // Словарь для кода
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * Метод генерации кода, для активации аккаунта
     * @return код для активации
     */
    public String generateCode() {
        // Создаем экземпляр класса Random
        Random random = new Random();

        // Создание StringBuilder длиной в 4 символа
        StringBuilder code = new StringBuilder(4);

        // Генерация кода
        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(ALPHANUMERIC.length());
            code.append(ALPHANUMERIC.charAt(index));
        }

        return code.toString();
    }
}
