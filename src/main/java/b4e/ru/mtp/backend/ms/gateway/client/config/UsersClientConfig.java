package b4e.ru.mtp.backend.ms.gateway.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class UsersClientConfig extends FeignClientConfig {

    protected UsersClientConfig(
            @Value("${ms.feign-clients.users-client.connect-timeout}") int connectTimeout,
            @Value("${ms.feign-clients.users-client.read-timeout}") int readTimeout,
            @Value("${ms.feign-clients.users-client.logger-level}") String loggerLevel,
            ObjectMapper mapper
    ) {
        super(connectTimeout, readTimeout, loggerLevel, mapper);
    }

}
