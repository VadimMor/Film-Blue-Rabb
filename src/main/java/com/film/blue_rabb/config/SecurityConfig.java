package com.film.blue_rabb.config;

import com.film.blue_rabb.utils.JwtTokenUtils;
import com.film.blue_rabb.utils.PasswordEncoder;
import com.film.blue_rabb.enums.RoleEnum;
import com.film.blue_rabb.exception.custom.AccessDeniedException;
import com.film.blue_rabb.service.Impl.UserServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;
    private final UserServiceImpl userService;
//    private final JwtRequestFilter jwtRequestFilter;
    private final JwtTokenUtils jwtTokenUtils;


    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter(jwtTokenUtils);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder.getEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setMaxAge(300000L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    @Bean
    public WebMvcConfigurer corsMappingConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .exposedHeaders("*");
            }
        };
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,"/place").hasAuthority(RoleEnum.ORGANIZATION.getAuthority())
                        .requestMatchers(HttpMethod.POST,"/place/hall").hasAuthority(RoleEnum.ORGANIZATION.getAuthority())
                        .requestMatchers(HttpMethod.POST,"/event").hasAuthority(RoleEnum.ORGANIZATION.getAuthority())
                        .requestMatchers(HttpMethod.POST,"/session").hasAuthority(RoleEnum.ORGANIZATION.getAuthority())
                        .requestMatchers("/client**").hasAuthority(RoleEnum.CLIENT.getAuthority())
                        .requestMatchers("/organization**").hasAuthority(RoleEnum.ORGANIZATION.getAuthority())
                        .requestMatchers(HttpMethod.POST, "/security/organization").hasAuthority(RoleEnum.ADMIN.getAuthority())
                        .requestMatchers("/error").permitAll() // Allow access to error endpoint
                        .anyRequest().permitAll()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> {
                            if (authException.getCause() instanceof ExpiredJwtException) {
                                throw (ExpiredJwtException) authException.getCause();
                            } else if (authException.getCause() instanceof AccessDeniedException) {
                                throw (AccessDeniedException) authException.getCause();
                            }
                            // Handle other authentication exceptions
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        })
                )
                .addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);

        http.authenticationProvider(daoAuthenticationProvider());

        return http.build();
    }
}
