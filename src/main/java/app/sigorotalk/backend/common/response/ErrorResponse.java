package app.sigorotalk.backend.common.response;

import lombok.Getter;
import java.util.Map;

@Getter
public class ErrorResponse {

    private final int code;
    private final String message;
    private final Map<String, String> validationErrors; // 상세 에러를 담을 필드

    /**
     * 일반적인 에러 응답을 위한 생성자 (기존과 동일)
     *
     * @param code    에러 코드
     * @param message 에러 메시지
     */
    public ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
        this.validationErrors = null; // 이 경우 validationErrors는 사용하지 않으므로 null
    }

    /**
     * 유효성 검사(Validation) 에러 응답을 위해 새로 추가한 생성자
     *
     * @param code             에러 코드
     * @param message          에러 메시지
     * @param validationErrors 필드별 상세 에러 내용
     */
    public ErrorResponse(int code, String message, Map<String, String> validationErrors) {
        this.code = code;
        this.message = message;
        this.validationErrors = validationErrors;
    }
}
