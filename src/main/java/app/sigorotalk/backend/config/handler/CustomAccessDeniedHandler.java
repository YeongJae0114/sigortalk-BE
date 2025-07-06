package app.sigorotalk.backend.config.handler;

import app.sigorotalk.backend.common.response.ApiResponse;
import app.sigorotalk.backend.common.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        String uri = request.getRequestURI();
        String clientIp = request.getRemoteAddr();

        log.warn("[ACCESS DENIED] {} 요청 거부 from IP: {}", uri, clientIp);

        // JSON 응답 작성
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
        response.setContentType("application/json;charset=UTF-8");

        ApiResponse<Void> apiResponse = ApiResponse.fail(
                new ErrorResponse(403, "허용되지 않은 API 요청입니다.")
        );

        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}
