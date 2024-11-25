package com.kamark.kamark.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kamark.kamark.dto.ErrorResponse;
import com.kamark.kamark.service.JWTUtils;
import com.kamark.kamark.service.OurUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private OurUserDetailsService ourUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String requestPath = request.getServletPath();

            List<String> allowedPrefixes = List.of("/auth", "/swagger", "/v3");

            boolean isAllowed = allowedPrefixes.stream().anyMatch(requestPath::startsWith);
            if (isAllowed) {
                filterChain.doFilter(request, response);
                return;
            }

            final String authHeader = request.getHeader("Authorization");
            final String jwtToken;
            final Integer userId;

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            jwtToken = authHeader.substring(7).trim();

            userId = jwtUtils.extractUserIdFromAccessToken(jwtToken);

            logger.info("Extracted userId from token: " + userId);
            logger.info("Current SecurityContext authentication: " + SecurityContextHolder.getContext().getAuthentication());

            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = ourUserDetailsService.loadUserById(userId);

                if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    logger.info("Uwierzytelniony użytkownik: " + userId);
                    logger.info("Role użytkownika: " + userDetails.getAuthorities());
                } else {
                    logger.info("Invalid token for user: " + userId);
                }
            }

            filterChain.doFilter(request, response);

            //filter catching exceptions here, because GlobalExceptionHandler as @ControllerAdvice doesn't detect filter exceptions
        } catch (ExpiredJwtException ex) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");
            response.getWriter().write(
                    new ObjectMapper().writeValueAsString(
                            new ErrorResponse(HttpStatus.FORBIDDEN.value(), "JWT expired: " + ex.getMessage())
                    ));
        } catch (SignatureException ex) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write(
                    new ObjectMapper().writeValueAsString(
                            new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Invalid JWT signature: " + ex.getMessage())
                    )
            );
        }

    }
}

