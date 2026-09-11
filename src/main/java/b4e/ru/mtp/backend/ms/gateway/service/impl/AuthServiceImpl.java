package b4e.ru.mtp.backend.ms.gateway.service.impl;

import b4e.ru.mtp.backend.commons.dto.auth.LoginUserRequest;
import b4e.ru.mtp.backend.commons.dto.auth.LoginUserResponse;
import b4e.ru.mtp.backend.commons.dto.auth.RegisterUserRequest;
import b4e.ru.mtp.backend.commons.dto.auth.RegisterUserResponse;
import b4e.ru.mtp.backend.commons.exception.MtpErrorCode;
import b4e.ru.mtp.backend.commons.exception.MtpException;
import b4e.ru.mtp.backend.ms.gateway.dto.UserCredentials;
import b4e.ru.mtp.backend.ms.gateway.service.AuthService;
import b4e.ru.mtp.backend.ms.gateway.service.JwtService;
import b4e.ru.mtp.backend.ms.gateway.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Value("${ms.security.autologin-when-registered}")
    private boolean autologinIsEnabled;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    @Override
    public RegisterUserResponse registerUser(RegisterUserRequest request) {
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setPassword(passwordEncoder.encode(request.getPassword()));
        userCredentials.setLogin(request.getLogin());

        UserCredentials user = userService.createUser(userCredentials);
        if (autologinIsEnabled) {
            return new RegisterUserResponse(jwtService.createJwtResponse(user));
        } else {
            return new RegisterUserResponse();
        }
    }

    @Override
    public LoginUserResponse loginUser(LoginUserRequest request) {
        UserCredentials user = userService.findUserByLogin(request.getLogin());
        if (authenticateUser(request.getLogin(), request.getPassword())) {
            return new LoginUserResponse(jwtService.createJwtResponse(user));
        } else {
            throw new MtpException(MtpErrorCode.AUTHENTICATION_BAD_CREDENTIALS);
        }
    }

    public Optional<UserCredentials> loadUserById(String id) throws UsernameNotFoundException {
        try {
            UserCredentials user = userService.findUserById(id);
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /** Аутентификация пользователя с возвратом статуса аутентификации
     *
     * @param login  логин
     * @param password пароль (сырой)
     * @return true если пользователь успешно аутентифицирован, иначе false
     */
    private boolean authenticateUser(String login, String password) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                login,
                password
        );

        try {
            Authentication auth = authenticationManager.authenticate(token);
            return auth.isAuthenticated();
        } catch (AuthenticationException e) {
            return false;
        }
    }


}