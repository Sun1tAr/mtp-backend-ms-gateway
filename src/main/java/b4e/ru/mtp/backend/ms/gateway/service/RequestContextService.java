package b4e.ru.mtp.backend.ms.gateway.service;


import b4e.ru.mtp.backend.ms.gateway.api.MDCConstants;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Service
public class RequestContextService {

    public void setUserId(String userId) {
        MDC.put(MDCConstants.USER_ID, userId);
    }

}
