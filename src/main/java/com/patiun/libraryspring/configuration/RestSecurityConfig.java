package com.patiun.libraryspring.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.patiun.libraryspring.user.Authority.*;

@Configuration
@EnableWebSecurity
public class RestSecurityConfig {

    private static final String BOOKS_URL_PATTERN = "/books/**";

    @Value("${allowed-cors.urls:http://localhost:3000}")
    private String[] allowedCorsUrls;

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthFilter jwtAuthFilter;

    @Autowired
    public RestSecurityConfig(AuthenticationEntryPoint authenticationEntryPoint, JwtAuthFilter jwtAuthFilter) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(HttpMethod.POST, BOOKS_URL_PATTERN).hasAuthority(ADD_BOOKS.name())
                        .requestMatchers(HttpMethod.POST, "/orders/**").hasAuthority(PLACE_ORDERS.name())
                        .requestMatchers(HttpMethod.GET, BOOKS_URL_PATTERN).permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/auth").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/users/change-password").authenticated()
                        .requestMatchers(HttpMethod.GET, "/users").hasAuthority(READ_USERS.name())
                        .requestMatchers(HttpMethod.GET, "/users/*").authenticated()
                        .requestMatchers(HttpMethod.GET, "/orders/**").hasAuthority(READ_ORDERS.name())
                        .requestMatchers(HttpMethod.PUT, BOOKS_URL_PATTERN).hasAuthority(EDIT_BOOKS.name())
                        .requestMatchers(HttpMethod.DELETE, BOOKS_URL_PATTERN).hasAuthority(EDIT_BOOKS.name())
                        .requestMatchers(HttpMethod.PUT, "/users/**").hasAuthority(EDIT_USERS.name())
                        .requestMatchers(HttpMethod.PATCH, "/orders/*/collect", "/orders/*/return").hasAuthority(COLLECT_ORDERS.name())
                        .requestMatchers(HttpMethod.PATCH, "/orders/*/approve", "/orders/*/decline").hasAuthority(JUDGE_ORDERS.name())
                        .anyRequest().permitAll()
                )
                .httpBasic((httpSecurityHttpBasicConfigurer) -> httpSecurityHttpBasicConfigurer.authenticationEntryPoint(authenticationEntryPoint))
                .cors().and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
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
                        .allowedOrigins(allowedCorsUrls)
                        .allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE");
            }
        };
    }
}