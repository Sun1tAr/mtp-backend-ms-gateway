package b4e.ru.mtp.backend.ms.gateway.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class JwtConfig {

    @Value("${ms.security.jwt.secret}")
    private String secret;

    @Bean
    public SecretKey secretKey() {
        byte[] decoded = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(decoded);
    }
}
