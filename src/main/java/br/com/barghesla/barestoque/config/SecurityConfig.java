package br.com.barghesla.barestoque.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Desabilita o CSRF (essencial para testes no Postman com POST, PUT, etc.)
            .csrf(csrf -> csrf.disable())
            // 2. Libera todos os endpoints para acesso público
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/**").permitAll() // "/**" significa tudo
            );
        return http.build();
    }
}

