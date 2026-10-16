package b4e.ru.mtp.backend.ms.gateway.config;

import b4e.ru.mtp.backend.ms.gateway.api.MtpHttpHeaders;
import b4e.ru.mtp.backend.ms.gateway.service.RequestContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    @Value("${ms.proxy-path.category-service-uri}")
    private String categoryServiceUri;

    @Value("${ms.proxy-path.user-service-uri}")
    private String userServiceUri;

    @Value("${ms.proxy-path.transaction-service-uri}")
    private String transactionServiceUri;

    @Value("${ms.proxy-path.account-service-uri}")
    private String accountServiceUri;

    private final RequestContextService requestContextService;

    @Bean
    public RouterFunction<ServerResponse> customRoutes() {
        return route("category-route")
                .GET("/categories/**", http())
                .POST("/categories/**", http())
                .before(uri(categoryServiceUri))
                .before(this::addUserIdHeader)
                .build()
                .and(route("user-route")
                        .POST("/users/**", http())
                        .GET("/users/**", http())
                        .before(uri(userServiceUri))
                        .before(this::addUserIdHeader)
                .build())
                .and(route("transaction-route")
                        .POST("/transactions/**", http())
                        .GET("/transactions/**", http())
                        .before(uri(transactionServiceUri))
                        .before(this::addUserIdHeader)
                .build())
                .and(route("account-route")
                        .POST("/accounts/**", http())
                        .GET("/accounts/**", http())
                        .before(uri(accountServiceUri))
                        .before(this::addUserIdHeader)
                .build())
                ;
    }

    private ServerRequest addUserIdHeader(ServerRequest request) {
        String userId = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            userId = auth.getPrincipal().toString();
        }

        // Добавляем заголовок в запрос перед отправкой
        return ServerRequest.from(request)
                .header(MtpHttpHeaders.USER_ID, userId)
                .build();
    }

}

