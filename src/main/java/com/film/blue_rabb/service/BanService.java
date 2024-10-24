package com.film.blue_rabb.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;

@Service
public interface BanService {
    /**
     * Метод проверки на бан ip
     * @param httpRequest данные http
     */
    void checkBanIp(HttpServletRequest httpRequest);
}
