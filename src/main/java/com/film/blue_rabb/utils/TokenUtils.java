package com.film.blue_rabb.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;

@Component
public class TokenUtils {
    @Value("${app.secret-key}")
    private String secret;

    @Value("${app.key-lifetime-minutes}")
    private Duration jwtLifetime;

    private final List<String> invalidToken = new ArrayList<>();

    private SecretKey key;

    /**
     * Создание токена авторизации
     * @param userDetails детаил пользователя
     * @return токен авторизации
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        //Получение ролей пользователя
        List<String> rolesList = userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        claims.put("roles", rolesList);

        //Дата начала и конца использования токена авторизации
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtLifetime.toMillis());

        //Создание токена авторизации
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(issuedDate)
                .expiration(expiredDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Получение логина пользователя из токена
     * @param token токен авторизации
     * @return логин пользователя
     */
    public String getLoginFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    /**
     * Получение ролей пользователя из токена авторизации
     * @param token токен авторизации
     * @return массив ролей пользователя
     */
    public List<String> getRolesFromToken(String token)  {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> claims = getAllClaimsFromToken(token);

        return objectMapper.convertValue(
                claims.get("roles"), new TypeReference<List<String>>() {}
        );
    }

    /**
     * Получение значений в токене
     * @param token токен авторизации
     * @return значения в токене
     */
    private Claims getAllClaimsFromToken(String token) {
        if (invalidToken.contains(token)) {
            return Jwts.claims().build();
        }

        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getBody();
    }

    /**
     * Инициализация ключа
     * @return зашифрованный ключ
     */
    private SecretKey getSigningKey() {
        if (key == null) {
            key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        }
        return key;
    }

}
