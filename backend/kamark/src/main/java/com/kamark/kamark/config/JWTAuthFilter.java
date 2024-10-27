package com.kamark.kamark.config;

import com.kamark.kamark.service.JWTUtils;
import com.kamark.kamark.service.OurUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private OurUserDetailsService ourUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String email;

        // Sprawdź, czy nagłówek Authorization istnieje i zaczyna się od "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Wyodrębnij token JWT, usuwając prefiks "Bearer "
        jwtToken = authHeader.substring(7);
        email = jwtUtils.extractEmail(jwtToken);

        // Logi dla diagnostyki
        logger.info("Extracted email from token: " + email);
        logger.info("Current SecurityContext authentication: " + SecurityContextHolder.getContext().getAuthentication());

        // Ustaw uwierzytelnienie, jeśli jeszcze nie istnieje
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = ourUserDetailsService.loadUserByUsername(email);

            // Sprawdź, czy token jest ważny
            if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Ustaw SecurityContextHolder z uwierzytelnieniem użytkownika
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                logger.info("Uwierzytelniony użytkownik: " + email);
                logger.info("Role użytkownika: " + userDetails.getAuthorities());
            } else {
                logger.info("Invalid token for user: " + email);
            }
        }

        filterChain.doFilter(request, response);
    }
}
