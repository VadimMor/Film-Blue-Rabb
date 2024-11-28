package com.film.blue_rabb.service.Impl;

import com.film.blue_rabb.exception.custom.BannedException;
import com.film.blue_rabb.model.Ban;
import com.film.blue_rabb.repository.BanRepository;
import com.film.blue_rabb.service.BanService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class BanServiceImpl implements BanService {
    private final BanRepository banRepository;

    private static final String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    /**
     * Метод проверки на бан ip
     * @param httpRequest данные http
     */
    @Override
    public void checkBanIp(HttpServletRequest httpRequest) throws BannedException {
        log.trace("BanServiceImpl.checkBanIp");

        // Проверка на бан ip адрес пользователя
        if (banRepository.findFirstByBannedIp(getClientIpAddress(httpRequest), OffsetDateTime.now())) {
            throw new BannedException();
        }
    }

    /**
     * Получение ip адрес клиента
     * @param httpRequest данные http
     * @return ip адрес
     */
    public static String getClientIpAddress(HttpServletRequest httpRequest) {
        log.trace("IpUtils.getClientIpAddress");

        for (String header : HEADERS_TO_TRY) {
            String ip = httpRequest.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }

        return httpRequest.getRemoteAddr();
    }
}
