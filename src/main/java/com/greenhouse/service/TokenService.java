package com.greenhouse.service;

import java.util.Date;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

    private static final String SECRET_KEY = "bimatnho-duongcongto";
    private static final long EXPIRATION_TIME_MS = 5 * 60 * 60 * 60; // Thời gian hiệu lực của token: 5 phút

    private List<String> invalidatedTokens = new ArrayList<>(); // Danh sách token bị vô hiệu hoá

    public String generateTokenByUsername(String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME_MS);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        return token;
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return !isTokenInvalidated(token); // Kiểm tra token chưa bị vô hiệu hoá
        } catch (Exception e) {
            return false;
        }
    }

    public void invalidateToken(String token) {
        if (!isTokenInvalidated(token)) {
            invalidatedTokens.add(token); // Thêm token vào danh sách bị vô hiệu hoá
        }
    }

    private boolean isTokenInvalidated(String token) {
        return invalidatedTokens.contains(token); // Kiểm tra token có trong danh sách bị vô hiệu hoá không
    }
}
