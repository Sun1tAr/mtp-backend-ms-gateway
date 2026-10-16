package b4e.ru.mtp.backend.ms.gateway.client;

import b4e.ru.mtp.backend.ms.gateway.api.dto.CreateUserRequest;
import b4e.ru.mtp.backend.ms.gateway.api.dto.CreateUserResponse;
import b4e.ru.mtp.backend.ms.gateway.api.dto.GetUserResponse;
import b4e.ru.mtp.backend.ms.gateway.client.config.UsersClientConfig;
import b4e.ru.mtp.backend.ms.gateway.client.fallback.UsersFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "users-client",
        configuration = UsersClientConfig.class,
        fallbackFactory = UsersFallbackFactory.class,
        url = "${ms.feign-clients.users-client.url}"
)
public interface UsersClient {

    @PostMapping("/users/new")
    ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest request);

    @GetMapping("/users/by-login")
    ResponseEntity<GetUserResponse> getUserByLogin(@RequestParam String login);

    @GetMapping("/users/by-id")
    ResponseEntity<GetUserResponse> getUserById(@RequestParam Long id);

}
