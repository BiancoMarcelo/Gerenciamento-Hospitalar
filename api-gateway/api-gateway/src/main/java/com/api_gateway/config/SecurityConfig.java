package com.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/actuator/**").permitAll()


                        .pathMatchers("/agendamento/api/cadastro/**").hasAnyAuthority("USUARIO", "MEDICO", "ADMIN")
                        .pathMatchers("/agendamento/api/agendamentos/**").hasAnyAuthority("USUARIO", "MEDICO", "ADMIN")

                        .pathMatchers("/clinica/api/clinica/atender-consulta").hasAnyAuthority("MEDICO", "ADMIN")
                        .pathMatchers("/clinica/api/clinica/**").hasAnyAuthority("USUARIO", "MEDICO", "ADMIN")

                        .pathMatchers("/medicina/api/procedimentos").hasAnyAuthority("MEDICO", "ADMIN")
                        .pathMatchers("/medicina/api/exames").hasAnyAuthority("USUARIO", "MEDICO", "ADMIN")

                        .pathMatchers("/admin/**").hasRole("ADMIN")

                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer((oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))));

        return http.build();
    }

    public Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        return jwt -> {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            Collection<GrantedAuthority> authorities = ((List<String>) realmAccess.get("roles"))
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            return Mono.just(new JwtAuthenticationToken(jwt, authorities));
        };
    }
}

