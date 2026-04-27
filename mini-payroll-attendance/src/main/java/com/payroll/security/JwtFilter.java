package com.payroll.security;

import com.payroll.model.SystemRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

// No @Component — instantiated directly in SecurityConfig to avoid
// Spring Boot auto-registering it outside the security filter chain
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                if (jwtUtil.validateToken(token)) {
                    UUID employeeId = jwtUtil.getEmployeeIdFromToken(token);
                    SystemRole systemRole = jwtUtil.getSystemRoleFromToken(token);

                    log.debug("JWT valid — employeeId={}, role={}", employeeId, systemRole);

                    SimpleGrantedAuthority authority =
                            new SimpleGrantedAuthority("ROLE_" + systemRole.name());

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    employeeId, null,
                                    Collections.singletonList(authority));

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                } else {
                    log.warn("JWT validation failed for: {} {}", request.getMethod(), request.getRequestURI());
                }
            } catch (Exception e) {
                log.error("JWT error for {} {}: {}", request.getMethod(), request.getRequestURI(), e.getMessage());
            }
        } else {
            log.debug("No Bearer token for: {} {}", request.getMethod(), request.getRequestURI());
        }

        filterChain.doFilter(request, response);
    }
}
