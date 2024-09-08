package com.film.blue_rabb.config;

import com.film.blue_rabb.exception.custom.BannedException;
import com.film.blue_rabb.service.BanService;
import com.film.blue_rabb.service.Impl.BanServiceImpl;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class IpBanFilter extends OncePerRequestFilter {

    private final BanService banService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            banService.checkBanIp(request);
        } catch (BannedException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Access is denied to the resource is closed");
            return;
        }

        filterChain.doFilter(request, response);
    }
}

