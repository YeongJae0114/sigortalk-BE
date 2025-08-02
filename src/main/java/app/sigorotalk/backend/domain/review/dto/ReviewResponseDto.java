package app.sigorotalk.backend.domain.review.dto;

import app.sigorotalk.backend.domain.review.Review;
import app.sigorotalk.backend.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ReviewResponseDto {

    private final Long id;
    private final String authorName; // 작성자 이름
    private final float rating;
    private final String comment;
    private final List<String> imageUrls;
    private final LocalDateTime createdAt;

    // Review 엔티티를 DTO로 변환하는 정적 메서드
    public static ReviewResponseDto from(Review review) {
        User user = review.getUser();
        String authorName = (user != null) ? user.getName() : "알 수 없음";

        return ReviewResponseDto.builder()
                .id(review.getId())
                .authorName(authorName)
                .rating(review.getRating())
                .comment(review.getComment())
                .imageUrls(review.getImageUrls())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
