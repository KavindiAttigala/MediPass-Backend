package com.sdgp.MediPass.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;

public class JwtUtil {
    private static final String SECRET = "YourSecretKeyReplaceThisWithAProperOne";
    private static final long EXPIRATION_TIME = 21600000; // 6 hours

    public static String generateToken(String mediId) {
        return Jwts.builder()
                .setSubject(mediId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public static String validateToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException e) {
            return null;
        }
    }
}
