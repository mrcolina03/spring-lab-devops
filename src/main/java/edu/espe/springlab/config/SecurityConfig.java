package edu.espe.springlab.config;

import edu.espe.springlab.security.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtils jwtUtils;

    public SecurityConfig(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    // Definición del filtro de seguridad principal
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Configuración base del sistema de seguridad
        http
                .csrf(config -> config.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers("/api/students/**").authenticated()
                        .anyRequest().permitAll()
                )
                // Se agrega el filtro JWT antes del filtro básico de autenticación
                .addFilterBefore(new JwtFilter(jwtUtils), BasicAuthenticationFilter.class);

        return http.build();
    }

    // Filtro personalizado encargado de procesar y validar el token JWT
    static class JwtFilter extends BasicAuthenticationFilter {
        private final JwtUtils jwtUtils;

        public JwtFilter(JwtUtils jwtUtils) {
            super(authentication -> authentication);
            this.jwtUtils = jwtUtils;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain chain)
                throws IOException, ServletException {

            // Se obtiene el encabezado Authorization del request
            String header = request.getHeader("Authorization");

            if (header != null && header.startsWith("Bearer ")) {
                // Se extrae el token eliminando el prefijo "Bearer "
                String token = header.substring(7);

                // Validación y procesamiento del token JWT
                if (jwtUtils.validateToken(token)) {
                    String username = jwtUtils.getUsernameFromToken(token);

                    var auth = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            Collections.emptyList()
                    );

                    // Se guarda la autenticación en el contexto de seguridad
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }

            // Continuar con la cadena de filtros
            chain.doFilter(request, response);
        }
    }
}
