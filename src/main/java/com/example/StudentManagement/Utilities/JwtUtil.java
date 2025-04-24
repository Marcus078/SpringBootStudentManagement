package com.example.StudentManagement.Utilities;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

import static io.jsonwebtoken.Jwts.*;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    //private String secretKey =// "RBip1mbHt3Eq4CXlf6+GdgdK1+epRW8+pZlEZwvK3Vs=";  // Ideally, this should be stored securely (e.g., in application properties)
    private String secretKey;

    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // Generate JWT Token with 1-hour expiry
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 ))  // Set token expiration (1 hour)
                .signWith(getSigningKey())
                .compact();
    }

    // Validate the JWT Token
    public boolean validateToken(String token, String email) {
        try {
            return email.equals(extractUsername(token)) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
    // Extract all claims
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extract the username (email) from the token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }


    // Check if expired
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Get expiration date
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }
}
