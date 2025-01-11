package com.example.customer.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    private long expirationTime = 86400000; // 24 hours

    // Generate a JWT token
    public String generateToken(String username) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes()); // Ensure correct secret key length

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Validate a JWT token
    public boolean validateToken(String token, String username) {
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }

    // Extract username from the token
    public String extractUsername(String token) {
        Claims claims = extractClaims(token);
        return claims.getSubject();
    }

    // Check if the token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date from the token
    private Date extractExpiration(String token) {
        Claims claims = extractClaims(token);
        return claims.getExpiration();
    }

    // Extract claims from the token using Jwts.parser() method (for version 0.12.6)
    private Claims extractClaims(String token) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());  // Ensure this key length is valid

        // Correct usage of Jwts.parser() in version 0.12.6
        return Jwts.parser()  // Use the parser method directly (not parserBuilder())
                .setSigningKey(key)  // Set the signing key for validating the JWT signature
                .parseClaimsJws(token)  // Parse the JWT token and get the claims
                .getBody();  // Extract and return the claims
    }
}
