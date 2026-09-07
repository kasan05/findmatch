package com.matrimony.findmatch.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(String email) { // Use email as username
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, email);
    }

    private String createToken(Map<String, Object> claims, String email){
        long currTime = System.currentTimeMillis();
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date(currTime))
                .expiration(new Date(currTime+expiration))
                .signWith(getSignKey())
                .compact();
    }


    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public Optional<String> validateAndGetToken(String token) {
        final String username = getUsernameFromToken(token);
        return username==null ? Optional.empty() :Optional.of(username);
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().verifyWith(getSignKey()).build()
                .parseSignedClaims(token).getPayload();
        if(claims.getExpiration().after(new Date())){
            return claims.getSubject();
        }else{
            return null;
        }
    }
}