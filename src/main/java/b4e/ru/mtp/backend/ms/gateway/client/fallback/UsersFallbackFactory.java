package b4e.ru.mtp.backend.ms.gateway.client.fallback;

import b4e.ru.mtp.backend.ms.gateway.api.dto.CreateUserRequest;
import b4e.ru.mtp.backend.ms.gateway.api.dto.CreateUserResponse;
import b4e.ru.mtp.backend.ms.gateway.api.dto.GetUserResponse;
import b4e.ru.mtp.backend.ms.gateway.client.UsersClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class UsersFallbackFactory extends MtpFeignClientFallbackFactory<UsersClient> {

    @Override
    public UsersClient create(Throwable cause) {
        return new UsersClient() {
            @Override
            public ResponseEntity<CreateUserResponse> createUser(CreateUserRequest request) {
                return (ResponseEntity<CreateUserResponse>) getResponseWithHeadedError(cause);
            }

            @Override
            public ResponseEntity<GetUserResponse> getUserByLogin(String login) {
                return (ResponseEntity<GetUserResponse>) getResponseWithHeadedError(cause);
            }

            @Override
            public ResponseEntity<GetUserResponse> getUserById(Long id) {
                return (ResponseEntity<GetUserResponse>) getResponseWithHeadedError(cause);
            }
        };
    }
}
