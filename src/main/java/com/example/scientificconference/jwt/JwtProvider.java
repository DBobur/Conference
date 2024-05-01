package com.example.scientificconference.jwt;

import com.example.scientificconference.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Random;
import java.util.StringJoiner;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    @Value("${secret.key}")
    private String secretKey;
    @SneakyThrows
    public String generate(UserEntity user)  {
        Integer password = new Random().nextInt(100000, 1000000);
        
        StringJoiner roles = new StringJoiner(",");
        user.getRoles().forEach(role -> roles.add(role.getName().toUpperCase()));
        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 600000 * 1000))
                .claim("roles", roles.toString())
                .claim("password",password.toString())
                .signWith(key())
                .compact();
    }

    private SecretKey key() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public boolean validate(final String token) {
        try {
            Claims claims = parse(token);
            if (claims.getExpiration().after(new Date())) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
