package com.kamark.kamark.service;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import com.kamark.kamark.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
public class JWTUtils {
    private static final Logger logger =
            (Logger) LoggerFactory.getLogger(JWTUtils.class);
    private final SecretKey key;
    private static final long TOKEN_EXPIRATION_TIME = 86400000; // 24 hours in milliseconds
    private static final String SECRET = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";

    public JWTUtils() {
        this.key = Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(User user) {
        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("roles", roles)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public String extractUserIdFromToken(String token) {
        return extractClaims(token, claims -> claims.get("userId", String.class));
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction){
        return claimsTFunction.apply(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload());
    }




    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        logger.info("Email from token: " + email);
        String userEmail = ((User) userDetails).getEmail();//rzutujemy na obiekt User i używamy metody User'a
        logger.info("Expected email: " + userEmail);
        logger.info("Token expired: " + isTokenExpired(token));

        return (email.equals(userEmail) && !isTokenExpired(token));
    }
    public boolean isTokenExpired(String token){
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

}
