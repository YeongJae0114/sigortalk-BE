package app.sigorotalk.backend.domain.review;

import app.sigorotalk.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements ErrorCode {
    REVIEW_ALREADY_EXISTS(HttpStatus.CONFLICT, 409, "리뷰는 한 번만 작성할 수 있습니다."),
    COFFEE_CHAT_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "리뷰를 작성하려는 커피챗을 찾을 수 없습니다."),
    INVALID_STATUS_FOR_REVIEW(HttpStatus.BAD_REQUEST, 400, "완료된 커피챗에만 리뷰를 작성할 수 있습니다."),
    NO_AUTHORITY_TO_REVIEW(HttpStatus.FORBIDDEN, 403, "리뷰를 작성할 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}