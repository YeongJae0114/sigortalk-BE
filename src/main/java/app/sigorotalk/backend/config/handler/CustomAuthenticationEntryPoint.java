package app.sigorotalk.backend.config.handler;

import app.sigorotalk.backend.common.response.ApiResponse;
import app.sigorotalk.backend.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String clientIp = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        log.warn("[AUTH FAIL] {} {} from IP: {} UA: {} → {}",
                method, uri, clientIp, userAgent, authException.getMessage());

        // JSON 응답
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
        response.setContentType("application/json;charset=UTF-8");

        ApiResponse<Void> apiResponse = ApiResponse.fail(
                new ErrorResponse(401, "인증이 필요합니다.")
        );
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}
