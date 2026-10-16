package b4e.ru.mtp.backend.ms.gateway.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegisterUserRequest {

    private String login;
    private String password;

}
