package app.sigorotalk.backend.common.exception;

import app.sigorotalk.backend.common.response.ApiResponse;
import app.sigorotalk.backend.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

import static app.sigorotalk.backend.common.exception.CommonErrorCode.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 인증 실패 예외 처리 (BadCredentialsException 등)
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(AuthenticationException e) {
        log.warn("인증 실패: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                CommonErrorCode.UNAUTHORIZED.getCode(),
                "이메일 또는 비밀번호가 일치하지 않습니다." // 사용자에게 보여줄 메시지
        );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED) // 401 상태 코드
                .body(ApiResponse.fail(errorResponse));
    }

    /**
     * 비즈니스 예외 처리
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBaseException(BusinessException e) {
        log.warn("Business Exception 발생: {}", e.getMessage());

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ApiResponse.fail(new ErrorResponse(e.getCode(), e.getMessage())));
    }

    /**
     * 데이터베이스 예외 처리
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<Void>> handleDatabaseException(DataAccessException e) {
        log.error("Database Error 발생", e); // ERROR 레벨
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail(new ErrorResponse(SYSTEM_ERROR.getCode(), SYSTEM_ERROR.getMessage())));
    }

    /**
     * 정의하지 않은 모든 예외를 서버 오류로 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(Exception e) {
        log.error("System Exception 발생", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail(new ErrorResponse(SYSTEM_ERROR.getCode(), SYSTEM_ERROR.getMessage())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        log.warn("Method Argument Not Valid Exception 발생: {}", e.getMessage());

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ErrorResponse errorResponse = new ErrorResponse(
                INVALID_PARAMETER.getCode(),
                INVALID_PARAMETER.getMessage(),
                errors
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(errorResponse));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.warn("Method Argument Type Mismatch Exception 발생: {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(new ErrorResponse(BAD_REQUEST.getCode(), BAD_REQUEST.getMessage())));
    }

}
