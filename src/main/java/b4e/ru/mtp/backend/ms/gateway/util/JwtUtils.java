package b4e.ru.mtp.backend.ms.gateway.util;

import b4e.ru.mtp.backend.ms.gateway.constant.JwtClaims;
import b4e.ru.mtp.backend.ms.gateway.domain.UserCredentials;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    public static String generateToken(UserCredentials user,
                                       Date expiresAt,
                                       SecretKey secretKey) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaims.EXPIRATION, expiresAt.getTime());
        claims.put(JwtClaims.USER_ID, String.valueOf(user.getId()));

        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claims(claims)
                .expiration(expiresAt)
                .signWith(secretKey)
                .compact();
    }

    private static Claims getClaimsFromToken(String token,
                                             SecretKey secretKey) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();


    }

    public static String getDataFromToken(String claim,
                                          String token,
                                          SecretKey secretKey) {
        Claims claims = getClaimsFromToken(token, secretKey);
        return claims.get(claim, String.class);
    }
}
