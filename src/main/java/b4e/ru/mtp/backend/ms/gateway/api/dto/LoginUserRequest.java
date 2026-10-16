package b4e.ru.mtp.backend.ms.gateway.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginUserRequest {

    private String login;
    private String password;

}
