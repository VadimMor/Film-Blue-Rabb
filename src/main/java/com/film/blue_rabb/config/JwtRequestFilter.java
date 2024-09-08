package com.film.blue_rabb.config;

import com.film.blue_rabb.utils.JwtTokenUtils;
import com.film.blue_rabb.exception.ErrorMessage;
import com.film.blue_rabb.exception.custom.AccessDeniedException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;


    private HandlerExceptionResolver resolver;

    @Autowired
    public void setHandlerExceptionResolver(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
        this.resolver = handlerExceptionResolver;
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String authHeader = request.getHeader("Authorization");
            String nickname = null;
            String jwt = null;

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwt = authHeader.substring(7);
                try {
                    nickname = jwtTokenUtils.getUsernameFromToken(jwt);
                } catch (ExpiredJwtException exception) {

                    List<ErrorMessage> trace = new ArrayList<>();
                    trace.add(new ErrorMessage("JwtRequestFilter.doFilterInternal", " Token lifetime is over This is Servlet Exception"));
                    log.error(trace.get(0).message());
                    throw new AccessDeniedException(exception.getHeader(), exception.getClaims(), trace);
                } catch (SignatureException e) {
                    log.error("Wrong signature");
                    throw new io.jsonwebtoken.security.SignatureException("Wrong signature");
                }
            }

            if (nickname != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        nickname,
                        null,
                        jwtTokenUtils.getRolesFromToken(jwt).stream().map(SimpleGrantedAuthority::new).toList()
                );

                SecurityContextHolder.getContext().setAuthentication(token);
            }

            filterChain.doFilter(request, response);
        }
        catch (Exception e ){
            resolver.resolveException(request, response, null, e);
        }

    }
}