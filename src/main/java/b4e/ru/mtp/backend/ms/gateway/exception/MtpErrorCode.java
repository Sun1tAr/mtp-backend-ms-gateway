package b4e.ru.mtp.backend.ms.gateway.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MtpErrorCode {

    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Произошла непредвиденная ошибка"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Переданы невалидные данные"),
    REQUEST_VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Данные не соответствуют политике валидации"),
    CLIENT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Непредвиденная ошибка работы с клиентом"),
    CLIENT_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "Вызываемый клиент недоступен"),
    AUTHENTICATION_JWT_INVALID(HttpStatus.UNAUTHORIZED, "Токен аутентификации поврежден"),
    AUTHENTICATION_USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "Пользователь не найден"),
    AUTHENTICATION_BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Неверные данные для входа"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    MtpErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
