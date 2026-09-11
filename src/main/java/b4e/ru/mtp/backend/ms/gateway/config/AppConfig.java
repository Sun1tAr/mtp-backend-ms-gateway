package b4e.ru.mtp.backend.ms.gateway.config;

import b4e.ru.mtp.backend.commons.config.FilterConfig;
import b4e.ru.mtp.backend.ms.gateway.client.UsersClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@EnableFeignClients(basePackageClasses = UsersClient.class)
@ComponentScan(basePackageClasses = {FilterConfig.class})
public class AppConfig {
}
