package com.pro_sb_ecommerce.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


// HERE WE GENERATED TOKEN WITH EMAIL AND ROLE
@Component
public class JwtUtil {

    private final String SECRET_KEY = "my_secret_key_12345";
    private final long JWT_EXPIRATION = 1000 * 60 * 60 * 10; // 10 hours

    //  Generate token with ROLE
    public String generateToken(String email, String role) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    //  Extract email
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //  Extract role
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    //  Validate token
    public boolean isTokenValid(String token, String email) {
        return extractEmail(token).equals(email) && !isTokenExpired(token);
    }

    //  Helpers
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}


// HERE WE GENERATED TOKEN BUT ONLY WITH EMAIL NOT ROLE

//@Component
//public class JwtUtil {
//
//    //  SECRET (must be at least 32 characters)
//    private static final String SECRET_KEY =
//            "my_super_secret_key_which_is_very_secure_12345";
//
//    //  Token validity (10 hours)
//    private static final long JWT_EXPIRATION =
//            1000 * 60 * 60 * 10;
//
//    private final SecretKey key =
//            Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
//
//    //  Generate token
//    public String generateToken(String email) {
//        return Jwts.builder()
//                .setSubject(email)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
//                .signWith(key)
//                .compact();
//    }
//
//    //  Extract email
//    public String extractEmail(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    //  Validate token
//    public boolean isTokenValid(String token, String email) {
//        return extractEmail(token).equals(email) && !isTokenExpired(token);
//    }
//
//    private boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    private Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        return resolver.apply(claims);
//    }
//}
//
//
