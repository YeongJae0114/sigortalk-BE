package app.sigorotalk.backend.domain.mentor;

import app.sigorotalk.backend.common.exception.BusinessException;
import app.sigorotalk.backend.common.exception.CommonErrorCode;
import app.sigorotalk.backend.domain.review.Review;
import app.sigorotalk.backend.domain.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 멘토의 평점 및 리뷰 수를 업데이트하는 DB 트랜잭션 로직을 전담하는 클래스
 */
@Component
@RequiredArgsConstructor
public class MentorRatingUpdater {

    private final MentorRepository mentorRepository;
    private final ReviewRepository reviewRepository;

    @Transactional // 이 메서드는 하나의 완벽한 작업 단위로 실행됨을 보장합니다.
    public void update(Long mentorId) {
        List<Review> reviews = reviewRepository.findByCoffeeChatApplicationMentorId(mentorId);
        if (reviews.isEmpty()) {
            return;
        }

        double average = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
        BigDecimal newAverageRating = BigDecimal.valueOf(average).setScale(1, RoundingMode.HALF_UP);

        Mentor mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new BusinessException(CommonErrorCode.NOT_FOUND));

        mentor.updateRating(newAverageRating, reviews.size());
    }
}