package b4e.ru.mtp.backend.ms.gateway.service;

import b4e.ru.mtp.backend.ms.gateway.client.dto.LoginUserRequest;
import b4e.ru.mtp.backend.ms.gateway.client.dto.LoginUserResponse;
import b4e.ru.mtp.backend.ms.gateway.client.dto.RegisterUserRequest;
import b4e.ru.mtp.backend.ms.gateway.client.dto.RegisterUserResponse;
import b4e.ru.mtp.backend.ms.gateway.domain.UserCredentials;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface AuthService {

    RegisterUserResponse registerUser(RegisterUserRequest request);

    LoginUserResponse loginUser(LoginUserRequest request);

    Optional<UserCredentials> loadUserById(String id) throws UsernameNotFoundException;

}
