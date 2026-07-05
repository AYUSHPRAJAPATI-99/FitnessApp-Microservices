package com.fitness.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
//Spring Boot में @Configuration class को आप manually call नहीं करते। Spring खुद उसे scan करके load करता है।
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain springsSecurityFilterChain(ServerHttpSecurity http){
        // jwt verify to har almost request ke liye honi chahiye so yaha jaruri hota nahi to har service me kro
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .anyExchange().permitAll()
                )
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .authorizeExchange(exchange -> exchange
//
//                        .pathMatchers(
//                                "/auth/**",
//                                "/oauth2/**",
//                                "/login/**"
//                        ).permitAll()
//
//                        .anyExchange().authenticated()
//                )
//                .oauth2ResourceServer(oauth2 ->
//                        oauth2.jwt(Customizer.withDefaults())
//                )
//                .build();

    }
}
