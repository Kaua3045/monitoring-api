package com.kaua.monitoring.infrastructure.configurations.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaua.monitoring.application.gateways.ProfileGateway;
import com.kaua.monitoring.infrastructure.utils.ApiError;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Collections;

@EnableWebSecurity
@Configuration
public class WebSecurityConfiguration implements WebMvcConfigurer {

    private static final String ADMIN_ROLE = "admin";

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ProfileGateway profileGateway;

    public WebSecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter, ProfileGateway profileGateway) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.profileGateway = profileGateway;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf()
                .disable()
                .cors()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
                .hasRole(ADMIN_ROLE)
                .requestMatchers("/auth", "/profile", "/error")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .build();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedOriginPatterns("*")
                .allowedMethods("*");
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            final var out = response.getOutputStream();

            new ObjectMapper().writeValue(out,
                    new ApiError("Token expired!", Collections.emptyList()));

            out.flush();
        };
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            final var aAccount = profileGateway.findByEmail(username);

            return new User(
                    aAccount.get().getEmail(),
                    aAccount.get().getPassword(),
                    true,
                    true,
                    true,
                    true,
                    new ArrayList<>()
            );
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
