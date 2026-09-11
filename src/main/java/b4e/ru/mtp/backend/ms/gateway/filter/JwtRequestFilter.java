package b4e.ru.mtp.backend.ms.gateway.filter;

import b4e.ru.mtp.backend.commons.api.MtpHttpHeaders;
import b4e.ru.mtp.backend.commons.dto.common.ErrorDtoResponse;
import b4e.ru.mtp.backend.commons.exception.MtpErrorCode;
import b4e.ru.mtp.backend.commons.service.RequestContextService;
import b4e.ru.mtp.backend.ms.gateway.dto.UserCredentials;
import b4e.ru.mtp.backend.ms.gateway.service.AuthService;
import b4e.ru.mtp.backend.ms.gateway.service.JwtService;
import b4e.ru.mtp.backend.ms.gateway.service.impl.AuthServiceImpl;
import b4e.ru.mtp.backend.ms.gateway.service.impl.JwtServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.RequestFacade;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private final JwtService jwtService;
    private final ObjectMapper mapper;
    private final RequestContextService requestContextService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {


        // если нет заголовка авторизации - скипаем
        if (request.getHeader(MtpHttpHeaders.HEADER_AUTH) == null ||
                !request.getHeader(MtpHttpHeaders.HEADER_AUTH).startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader(MtpHttpHeaders.HEADER_AUTH).substring(7);

        // если пользователь в контекст не добавлен - пробуем добавить
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            if (!jwtService.tokenIsValid(token)) {
                putErrorToResponseBody(response, MtpErrorCode.AUTHENTICATION_JWT_INVALID);
                return;
            }

            Optional<UserCredentials> userOpt = authService.loadUserById(jwtService.getIdFromJwt(token));
            if (userOpt.isEmpty()) {
                putErrorToResponseBody(response, MtpErrorCode.AUTHENTICATION_USER_NOT_FOUND);
            } else {
                Authentication auth = new UsernamePasswordAuthenticationToken(
                        userOpt.get().getId(),
                        token,
                        null
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
                requestContextService.setUserId(String.valueOf(userOpt.get().getId()));
            }
        }
        filterChain.doFilter(request, response);
    }

    private void putErrorToResponseBody(HttpServletResponse response, MtpErrorCode code) {
        response.setStatus(code.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorDtoResponse errorDtoResponse = new ErrorDtoResponse();
        errorDtoResponse.setCode(code);
        String jsonResponse = mapper.writeValueAsString(errorDtoResponse);

        try {
            response.getWriter().write(jsonResponse);
            response.getWriter().flush();
        } catch (IOException e) {
            log.error("Не удалось обновить тело ответа: {}", e.getMessage());
        }
    }


}
