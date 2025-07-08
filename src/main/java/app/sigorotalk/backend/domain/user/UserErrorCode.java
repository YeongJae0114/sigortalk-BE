package app.sigorotalk.backend.domain.user;

import app.sigorotalk.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, 409, "이미 존재하는 이메일입니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
