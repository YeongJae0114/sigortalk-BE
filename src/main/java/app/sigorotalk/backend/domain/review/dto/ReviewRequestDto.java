package app.sigorotalk.backend.domain.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewRequestDto {

    @NotNull(message = "커피챗 ID는 필수입니다.")
    private Long chatId; // 어떤 커피챗에 대한 리뷰인지 식별

    @NotNull(message = "평점은 필수입니다.")
    @Min(value = 1, message = "평점은 1 이상이어야 합니다.")
    @Max(value = 5, message = "평점은 5 이하여야 합니다.")
    private Integer rating; // 1~5점

    @NotBlank(message = "리뷰 내용은 필수입니다.")
    private String comment; // 리뷰 코멘트
}