package b4e.ru.mtp.backend.ms.gateway.service.impl;

import b4e.ru.mtp.backend.ms.gateway.client.dto.JwtAuthDto;
import b4e.ru.mtp.backend.ms.gateway.constant.JwtClaims;
import b4e.ru.mtp.backend.ms.gateway.domain.UserCredentials;
import b4e.ru.mtp.backend.ms.gateway.service.JwtService;
import b4e.ru.mtp.backend.ms.gateway.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final SecretKey secretKey;

    @Value("${ms.security.jwt.access-lifetime}")
    private Duration jwtLifetime;

    @Override
    public JwtAuthDto createJwtResponse(UserCredentials credentials) {
        String accessToken = getJwtForUser(credentials);
        return new JwtAuthDto(accessToken);
    }

    @Override
    public boolean tokenIsValid(String token) {
        try {
            getIdFromJwt(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


//    @Override
    public String getIdFromJwt(String token) {
        return JwtUtils.getDataFromToken(JwtClaims.USER_ID, token, secretKey);
    }

    private String getJwtForUser(UserCredentials user) {
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + jwtLifetime.toMillis());
        return JwtUtils.generateToken(user, expiresAt, secretKey);
    }

}
