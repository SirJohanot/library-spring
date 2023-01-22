package com.patiun.libraryspring.mode.rest.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class RestSecurityConfig {

    public static final String FRONT_END_URL = "http://localhost:3000";

    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    public RestSecurityConfig(AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(HttpMethod.POST, "/books/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/orders/**").hasAuthority("READER")
                        .requestMatchers(HttpMethod.GET, "/books/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/users/auth").authenticated()
                        .requestMatchers(HttpMethod.GET, "/users/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/orders/**").hasAnyAuthority("READER", "LIBRARIAN")
                        .requestMatchers(HttpMethod.PUT, "/books/**", "/users/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/orders/*/collect", "/orders/*/return").hasAuthority("READER")
                        .requestMatchers(HttpMethod.PUT, "/orders/*/approve", "/orders/*/decline").hasAuthority("LIBRARIAN")
                        .requestMatchers(HttpMethod.DELETE, "/books/**").hasAuthority("ADMIN")
                        .anyRequest().permitAll()
                )
                .httpBasic((httpSecurityHttpBasicConfigurer) -> httpSecurityHttpBasicConfigurer.authenticationEntryPoint(authenticationEntryPoint))
                .cors().and()
                .build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@Nullable CorsRegistry registry) {
                if (registry == null) {
                    return;
                }
                registry.addMapping("/**")
                        .allowedOrigins(FRONT_END_URL)
                        .allowedMethods("GET", "POST", "PUT", "DELETE");
            }
        };
    }
}
