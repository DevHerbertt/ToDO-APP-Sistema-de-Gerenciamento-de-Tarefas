package com.ToDoAPI.ToDo_APi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Log4j2
@Component
public class FilterJwt extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.debug("=== JWT FILTER STARTED ===");
        log.debug("Request URL: {}", request.getRequestURL());
        log.debug("Request Method: {}", request.getMethod());

        String authHeader = request.getHeader("Authorization");
        log.debug("Authorization Header: {}", authHeader);

        // Permitir login e register sem token
        if (request.getRequestURL().toString().contains("/api/users/login") ||
                request.getRequestURL().toString().contains("/api/users/register")) {
            log.debug("Skipping JWT filter for public auth endpoints.");
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Missing or invalid Authorization header.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid authorization header");
            return;
        }

        String token = authHeader.substring(7);
        log.debug("Token received: {}", token.length() > 50 ? token.substring(0, 50) + "..." : token);

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            String role = claims.get("role", String.class);

            log.info("Valid token for user: {}", username);
            log.info("User role: {}", role);

            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

            var authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                    username, null, Arrays.asList(authority));

            org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token: " + e.getMessage());
        }
    }
}
