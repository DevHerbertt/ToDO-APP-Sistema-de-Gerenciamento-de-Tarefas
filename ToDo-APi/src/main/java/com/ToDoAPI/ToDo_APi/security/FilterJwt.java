package com.ToDoAPI.ToDo_APi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class FilterJwt extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("=== JWT FILTER ===");
        System.out.println("URL: " + request.getRequestURL());
        System.out.println("METHOD: " + request.getMethod());

        String authHeader = request.getHeader("Authorization");
        System.out.println("AUTH HEADER: " + authHeader);

        // ✅ PERMITIR LOGIN E REGISTER SEM TOKEN
        if (request.getRequestURL().toString().contains("/api/users/login") ||
                request.getRequestURL().toString().contains("/api/users/register")) {
            System.out.println("✅ Skipping JWT filter for auth endpoints");
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ No auth header or not Bearer");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid authorization header");
            return;
        }

        String token = authHeader.substring(7);
        System.out.println("Token: " + (token.length() > 50 ? token.substring(0, 50) + "..." : token));

        try {
            // ✅ VALIDAR TOKEN DIRETAMENTE AQUI
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            String role = claims.get("role", String.class);

            System.out.println("✅ Valid token for user: " + username);
            System.out.println("✅ User role: " + role);

            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

            org.springframework.security.authentication.UsernamePasswordAuthenticationToken authentication =
                    new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                            username, null, Arrays.asList(authority));

            org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            System.out.println("❌ Token validation error: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token: " + e.getMessage());
        }
    }
}