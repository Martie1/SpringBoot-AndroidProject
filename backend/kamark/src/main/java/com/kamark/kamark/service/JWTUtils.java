package com.kamark.kamark.service;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import com.kamark.kamark.entity.UserEntity;
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

    private final SecretKey accessTokenKey;
    private final SecretKey refreshTokenKey;

    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 3600000; // 1 h
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 604800000; // 7 days

    private static final String ACCESS_TOKEN_SECRET = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
    private static final String REFRESH_TOKEN_SECRET = "12987654327654561234897856343213233456R5678908765TG5678X348967R232147F646D9";

    public JWTUtils() {
        this.accessTokenKey = Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes());
        this.refreshTokenKey = Keys.hmacShaKeyFor(REFRESH_TOKEN_SECRET.getBytes());
    }

    public String generateAccessToken(UserEntity user) {
        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("roles", roles)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(accessTokenKey)
                .compact();
    }
    public String generateRefreshToken(UserEntity user) {

        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(refreshTokenKey)
                .compact();
    }

    public boolean isAccessTokenExpired(String token){
        return extractClaimsFromAccess(token, Claims::getExpiration).before(new Date());
    }
    public boolean isRefreshTokenExpired(String token){
        return extractClaimsFromRefresh(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaimsFromAccess(String token, Function<Claims, T> claimsTFunction){
        return claimsTFunction.apply(Jwts.parser().verifyWith(accessTokenKey).build().parseSignedClaims(token).getPayload());
    }
    private <T> T extractClaimsFromRefresh(String token, Function<Claims, T> claimsTFunction){
        return claimsTFunction.apply(Jwts.parser().verifyWith(refreshTokenKey).build().parseSignedClaims(token).getPayload());
    }

    public Integer extractUserIdFromAccessToken(String token) {
        return Integer.parseInt(extractClaimsFromAccess(token, Claims::getSubject));
    }
    public Integer extractUserIdFromAuthorizationHeader(String authHeader){
        String token = authHeader.substring(7);
        return extractUserIdFromAccessToken(token);
    }

    public Integer extractUserIdFromRefreshToken(String token) {
        return Integer.parseInt(extractClaimsFromRefresh(token, Claims::getSubject));
    }

    //for both refresh and access token
    public boolean isTokenValid(String token, UserDetails userDetails) {
        Integer userIdFromToken = extractUserIdFromAccessToken(token);
        Integer userId = ((UserEntity) userDetails).getId();
        logger.info("UserId from token: " + userIdFromToken);
        logger.info("Expected UserId: " + userId);
        logger.info("Token expired: " + isAccessTokenExpired(token));
        return (userIdFromToken.equals(userId) && !isAccessTokenExpired(token));
    }
    public boolean isRefreshTokenValid(String refreshToken, UserDetails userDetails) {
        Integer userIdFromToken = extractUserIdFromRefreshToken(refreshToken);
        Integer userId = ((UserEntity) userDetails).getId();
        return (userIdFromToken.equals(userId) && !isRefreshTokenExpired(refreshToken));
    }




}
