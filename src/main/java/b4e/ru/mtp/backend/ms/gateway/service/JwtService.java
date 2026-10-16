package b4e.ru.mtp.backend.ms.gateway.service;

import b4e.ru.mtp.backend.ms.gateway.client.dto.JwtAuthDto;
import b4e.ru.mtp.backend.ms.gateway.domain.UserCredentials;

public interface JwtService {

    JwtAuthDto createJwtResponse(UserCredentials credentials);

    boolean tokenIsValid(String token);

    String getIdFromJwt(String token);


}
