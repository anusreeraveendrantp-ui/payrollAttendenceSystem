package com.payroll.config;

import com.payroll.security.JwtFilter;
import com.payroll.security.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    // Inject JwtUtil directly — JwtFilter is no longer a @Component
    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtUtil);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
    "http://localhost:4200",
    "http://localhost:3000",
    "https://payroll-frontend-tau.vercel.app",
    "https://payroll-frontend-ecmotf9ee-anusree-raveendran-t-ps-projects.vercel.app"
));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/api/auth/login").permitAll()

                // Admin-only endpoints
                .requestMatchers(HttpMethod.POST, "/api/employees").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/attendance").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/leaves/*/approve").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/leaves/*/reject").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/leaves").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/employees").hasRole("ADMIN")

                // Authenticated endpoints
                .requestMatchers("/api/**").authenticated()

                .anyRequest().permitAll()
            )
            .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
