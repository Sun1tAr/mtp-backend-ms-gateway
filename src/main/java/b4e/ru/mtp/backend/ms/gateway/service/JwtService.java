package b4e.ru.mtp.backend.ms.gateway.service;

import b4e.ru.mtp.backend.ms.gateway.api.dto.JwtAuthDto;
import b4e.ru.mtp.backend.ms.gateway.dto.UserCredentials;

public interface JwtService {

    JwtAuthDto createJwtResponse(UserCredentials credentials);

    boolean tokenIsValid(String token);

    String getIdFromJwt(String token);


}
