package b4e.ru.mtp.backend.ms.gateway.service.impl;

import b4e.ru.mtp.backend.ms.gateway.api.MtpHttpHeaders;
import b4e.ru.mtp.backend.ms.gateway.api.dto.CreateUserRequest;
import b4e.ru.mtp.backend.ms.gateway.api.dto.CreateUserResponse;
import b4e.ru.mtp.backend.ms.gateway.api.dto.GetUserResponse;
import b4e.ru.mtp.backend.ms.gateway.client.UsersClient;
import b4e.ru.mtp.backend.ms.gateway.dto.UserCredentials;
import b4e.ru.mtp.backend.ms.gateway.exception.MtpErrorCode;
import b4e.ru.mtp.backend.ms.gateway.exception.MtpException;
import b4e.ru.mtp.backend.ms.gateway.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UsersClient usersClient;

    @Override
    public UserCredentials createUser(UserCredentials userCredentials) {
        CreateUserRequest request = new CreateUserRequest();
        request.setLogin(userCredentials.getLogin());
        request.setPasswordHash(userCredentials.getPassword());

        ResponseEntity<?> response = usersClient.createUser(request);
        validateResponse(response);
        CreateUserResponse responseBody = (CreateUserResponse) response.getBody();

        UserCredentials credentials = new UserCredentials();
        credentials.setId(responseBody.getUserId());

        return credentials;
    }

    @Override
    public UserCredentials findUserByLogin(String login) {
        ResponseEntity<?> response = usersClient.getUserByLogin(login);
        validateResponse(response);
        GetUserResponse responseBody = (GetUserResponse) response.getBody();

        UserCredentials credentials = new UserCredentials();
        credentials.setLogin(responseBody.getLogin());
        credentials.setPassword(responseBody.getPassword());
        credentials.setId(responseBody.getId());

        return credentials;
    }

    @Override
    public UserCredentials findUserById(String id) {
        ResponseEntity<?> response = usersClient.getUserById(Long.parseLong(id));
        validateResponse(response);
        GetUserResponse responseBody = (GetUserResponse) response.getBody();

        UserCredentials credentials = new UserCredentials();
        credentials.setLogin(responseBody.getLogin());
        credentials.setPassword(responseBody.getPassword());
        credentials.setId(responseBody.getId());

        return credentials;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findUserByLogin(username);
    }

    private void validateResponse(ResponseEntity<?> response) {
        if (response.getStatusCode().is2xxSuccessful()) {//fixme
        } else if (response.getHeaders().get(MtpHttpHeaders.ERROR_CODE).isEmpty()) {
            throw new MtpException(MtpErrorCode.CLIENT_ERROR);
        } else {
            List<String> errorCode = response.getHeaders().get(MtpHttpHeaders.ERROR_CODE);
            MtpErrorCode mtpErrorCode = MtpErrorCode.valueOf(errorCode.getFirst());
            throw new MtpException(mtpErrorCode);
        }
    }

}
