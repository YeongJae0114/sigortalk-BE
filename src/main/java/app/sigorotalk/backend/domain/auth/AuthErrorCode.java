package app.sigorotalk.backend.domain.auth;

import app.sigorotalk.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, 401, "유효하지 않은 리프레시 토큰입니다."),
    REFRESH_TOKEN_NOT_MATCH(HttpStatus.FORBIDDEN, 403, "리프레시 토큰이 일치하지 않습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}