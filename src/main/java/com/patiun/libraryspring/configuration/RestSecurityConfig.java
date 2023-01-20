package com.patiun.libraryspring.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@ConditionalOnProperty(prefix = "mvc.controller",
        name = "enabled",
        havingValue = "false",
        matchIfMissing = true)
public class RestSecurityConfig {

    public static final String FRONT_END_URL = "*";

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

}
