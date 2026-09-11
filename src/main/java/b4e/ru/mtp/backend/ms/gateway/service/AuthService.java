package b4e.ru.mtp.backend.ms.gateway.service;

import b4e.ru.mtp.backend.commons.dto.auth.LoginUserRequest;
import b4e.ru.mtp.backend.commons.dto.auth.LoginUserResponse;
import b4e.ru.mtp.backend.commons.dto.auth.RegisterUserRequest;
import b4e.ru.mtp.backend.commons.dto.auth.RegisterUserResponse;
import b4e.ru.mtp.backend.commons.exception.MtpErrorCode;
import b4e.ru.mtp.backend.commons.exception.MtpException;
import b4e.ru.mtp.backend.ms.gateway.dto.UserCredentials;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

public interface AuthService {

    RegisterUserResponse registerUser(RegisterUserRequest request);

    LoginUserResponse loginUser(LoginUserRequest request);

    Optional<UserCredentials> loadUserById(String id) throws UsernameNotFoundException;

}
