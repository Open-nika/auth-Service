package com.OpenNika.AuthService.Utility;


import java.util.Date;


import org.springframework.stereotype.Component;

import com.OpenNika.AuthService.Dto.Role;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
@Component

public class JwtUtility {
    
     private static final String SECRET_KEY =
            "thisIsASecretKeyThatIsAtLeast32BytesLongForHS256";

    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    private static final Key SIGNING_KEY =
            Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(String userId,Role role) {
        return Jwts.builder()
                .setSubject(userId)
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public Role extractRole(String token) {
    String role = Jwts.parserBuilder()
            .setSigningKey(SIGNING_KEY)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("role", String.class);

    return Role.valueOf(role);
}

    public String extractUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

     public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
