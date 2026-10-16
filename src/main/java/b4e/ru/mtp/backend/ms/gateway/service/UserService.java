package b4e.ru.mtp.backend.ms.gateway.service;

import b4e.ru.mtp.backend.ms.gateway.domain.UserCredentials;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserCredentials createUser(UserCredentials userCredentials);
    UserCredentials findUserByLogin(String login);
    UserCredentials findUserById(String id);

}
