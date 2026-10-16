package b4e.ru.mtp.backend.ms.gateway.service;

import b4e.ru.mtp.backend.ms.gateway.api.dto.LoginUserRequest;
import b4e.ru.mtp.backend.ms.gateway.api.dto.LoginUserResponse;
import b4e.ru.mtp.backend.ms.gateway.api.dto.RegisterUserRequest;
import b4e.ru.mtp.backend.ms.gateway.api.dto.RegisterUserResponse;
import b4e.ru.mtp.backend.ms.gateway.dto.UserCredentials;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface AuthService {

    RegisterUserResponse registerUser(RegisterUserRequest request);

    LoginUserResponse loginUser(LoginUserRequest request);

    Optional<UserCredentials> loadUserById(String id) throws UsernameNotFoundException;

}
