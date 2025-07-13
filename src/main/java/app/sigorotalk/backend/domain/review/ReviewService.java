package app.sigorotalk.backend.domain.review;

import app.sigorotalk.backend.common.exception.BusinessException;
import app.sigorotalk.backend.domain.coffeechat.CoffeeChatApplication;
import app.sigorotalk.backend.domain.coffeechat.CoffeeChatApplicationRepository;
import app.sigorotalk.backend.domain.review.dto.ReviewRequestDto;
import app.sigorotalk.backend.domain.review.dto.ReviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CoffeeChatApplicationRepository coffeeChatApplicationRepository;
    private final ApplicationEventPublisher eventPublisher; // 이벤트 발행기

    @Transactional
    public ReviewResponseDto createReview(ReviewRequestDto requestDto, Long menteeId) {
        // 1. 중복 리뷰 작성 방지
        if (reviewRepository.existsByCoffeeChatApplicationId(requestDto.getChatId())) {
            throw new BusinessException(ReviewErrorCode.REVIEW_ALREADY_EXISTS);
        }

        // 2. 커피챗 정보 조회
        CoffeeChatApplication coffeeChat = coffeeChatApplicationRepository.findById(requestDto.getChatId())
                .orElseThrow(() -> new BusinessException(ReviewErrorCode.COFFEE_CHAT_NOT_FOUND));

        // 3. 상태 검증: COMPLETED 상태가 아니면 리뷰 작성 불가
        if (coffeeChat.getStatus() != CoffeeChatApplication.Status.COMPLETED) {
            throw new BusinessException(ReviewErrorCode.INVALID_STATUS_FOR_REVIEW);
        }

        // 4. 권한 검증: 요청자가 해당 커피챗의 멘티가 맞는지 확인
        if (!Objects.equals(coffeeChat.getMentee().getId(), menteeId)) {
            throw new BusinessException(ReviewErrorCode.NO_AUTHORITY_TO_REVIEW);
        }

        // Review 엔티티 생성 및 저장
        Review newReview = Review.builder()
                .coffeeChatApplication(coffeeChat)
                .rating(requestDto.getRating())
                .comment(requestDto.getComment())
                .build();
        reviewRepository.save(newReview);

        // 5. 이벤트 발행!
        eventPublisher.publishEvent(new ReviewCreatedEvent(coffeeChat.getMentor().getId()));

        return ReviewResponseDto.from(newReview);
    }
}
