package com.patiun.libraryspring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/books/**", "/", "/book/**").authenticated()
                        .requestMatchers("/add-book", "/edit-book/**", "/edit-book", "/users/**", "/user/**", "/edit-user/**", "/edit-user", "/switch-user-blocked").hasAuthority("ADMIN")
                        .requestMatchers("/place-order", "/collect-order", "/return-order").hasAuthority("READER")
                        .requestMatchers("/approve-order", "/decline-order").hasAuthority("LIBRARIAN")
                        .requestMatchers("/orders/**", "/order/**").hasAnyAuthority("READER", "LIBRARIAN")
                        .anyRequest().permitAll()
                )
                .formLogin((form) -> form
                        .loginPage("/sign-in")
                        .loginProcessingUrl("/sign-in")
                        .usernameParameter("login")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/", false)
                        .failureUrl("/sign-in?error=true")
                )
                .logout(logout -> logout
                        .logoutUrl("/sign-out")
                        .logoutSuccessUrl("/sign-in")
                        .invalidateHttpSession(true)
                );
        http.csrf().disable();
        return http.build();
    }

}
