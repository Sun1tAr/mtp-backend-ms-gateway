package b4e.ru.mtp.backend.ms.gateway.controller;


import b4e.ru.mtp.backend.commons.dto.auth.LoginUserRequest;
import b4e.ru.mtp.backend.commons.dto.auth.LoginUserResponse;
import b4e.ru.mtp.backend.commons.dto.auth.RegisterUserRequest;
import b4e.ru.mtp.backend.commons.dto.auth.RegisterUserResponse;
import b4e.ru.mtp.backend.ms.gateway.service.AuthService;
import b4e.ru.mtp.backend.ms.gateway.service.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserRequest request) {
        RegisterUserResponse registerUserResponse = authService.registerUser(request);
        return ResponseEntity.ok(registerUserResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginUserRequest request) {
        LoginUserResponse loginUserResponse = authService.loginUser(request);
        return ResponseEntity.ok(loginUserResponse);
    }

}
