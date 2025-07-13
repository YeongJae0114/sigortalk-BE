package app.sigorotalk.backend.domain.mentor;

import app.sigorotalk.backend.common.exception.BusinessException;
import app.sigorotalk.backend.common.exception.CommonErrorCode;
import app.sigorotalk.backend.domain.mentor.dto.MentorDetailResponseDto;
import app.sigorotalk.backend.domain.mentor.dto.MentorListResponseDto;
import app.sigorotalk.backend.domain.review.Review;
import app.sigorotalk.backend.domain.review.ReviewCreatedEvent;
import app.sigorotalk.backend.domain.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MentorService {

    private final MentorRepository mentorRepository;
    private final ReviewRepository reviewRepository;

    public Page<MentorListResponseDto> getMentorList(String region, String expertise, Pageable pageable) {
        return mentorRepository.findByFilters(region, expertise, pageable)
                .map(MentorListResponseDto::from);
    }

    public MentorDetailResponseDto getMentorDetail(Long mentorId) {
        Mentor mentor = mentorRepository.findByIdWithUser(mentorId)
                .orElseThrow(() -> new BusinessException(CommonErrorCode.NOT_FOUND));

        // 멘토의 리뷰 목록 조회
        List<Review> reviews = reviewRepository.findByCoffeeChatApplicationMentorId(mentorId);

        return MentorDetailResponseDto.from(mentor, reviews);
    }

    // TODO: (관리자용) 멘토 등록 로직 구현

    /**
     * ReviewCreatedEvent를 수신하여 멘토의 평점과 리뷰 수를 업데이트합니다.
     *
     * @Async 를 통해 이 로직은 별도의 스레드에서 비동기적으로 실행됩니다.
     */
    @Async
    @EventListener
    @Transactional
    public void handleReviewCreatedEvent(ReviewCreatedEvent event) {
        Long mentorId = event.getMentorId();

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
