package org.example.eventmanager.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.eventmanager.users.domain.model.UserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenManager {

    private final Long tokenLifeTime;
    private final Key signKey;

    public JwtTokenManager(
            @Value("${jwt.sign-key}") String signKey,
            @Value("${jwt.lifetime}") Long tokenLifeTime) {
        this.signKey = new SecretKeySpec(signKey.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
        this.tokenLifeTime = tokenLifeTime;
    }

    public String generateToken(UserInfo user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());
        Date issuedTime = new Date();
        Date expirationTime = new Date(issuedTime.getTime() + tokenLifeTime);
        return Jwts.builder()
                .claims(claims)
                .subject(user.getLogin())
                .issuedAt(issuedTime)
                .expiration(expirationTime)
                .signWith(signKey)
                .compact();
    }

    public boolean isTokenValid(String jwtToken) {
        try{
            Jwts.parser()
                    .setSigningKey(signKey)
                    .build()
                    .parse(jwtToken);
        }catch (Exception e) {
            return false;
        }
        return true;
    }

    public String getLoginFromToken(String jwtToken) {
        return Jwts.parser()
                .setSigningKey(signKey)
                .build()
                .parseClaimsJws(jwtToken)
                .getPayload()
                .getSubject();
    }

    public String getRoleFromToken(String jwtToken) {
        return Jwts.parser()
                .setSigningKey(signKey)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody()
                .get("role", String.class);
    }
}
