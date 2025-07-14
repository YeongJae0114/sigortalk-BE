package app.sigorotalk.backend.domain.review;

import app.sigorotalk.backend.common.exception.BusinessException;
import app.sigorotalk.backend.domain.coffeechat.CoffeeChatApplication;
import app.sigorotalk.backend.domain.coffeechat.CoffeeChatApplicationRepository;
import app.sigorotalk.backend.domain.mentor.Mentor;
import app.sigorotalk.backend.domain.review.dto.ReviewRequestDto;
import app.sigorotalk.backend.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private CoffeeChatApplicationRepository coffeeChatApplicationRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    private User mentee;
    private Mentor mentor;
    private CoffeeChatApplication completedChat;

    @BeforeEach
    void setUp() {
        mentee = User.builder().id(1L).build();
        mentor = Mentor.builder().id(10L).build();
        completedChat = CoffeeChatApplication.builder()
                .id(100L)
                .mentee(mentee)
                .mentor(mentor)
                .status(CoffeeChatApplication.Status.COMPLETED)
                .build();
    }

    @Test
    @DisplayName("성공: 리뷰 작성 시, 리뷰 저장 및 이벤트 발행이 정상적으로 호출된다.")
    void createReview_Success() {
        // given
        ReviewRequestDto requestDto = createReviewRequestDto(completedChat.getId(), 5, "최고의 멘토링!");
        when(reviewRepository.existsByCoffeeChatApplicationId(completedChat.getId())).thenReturn(false);
        when(coffeeChatApplicationRepository.findById(completedChat.getId())).thenReturn(Optional.of(completedChat));

        // when
        reviewService.createReview(requestDto, mentee.getId());

        // then
        verify(reviewRepository, times(1)).save(any(Review.class));
        verify(eventPublisher, times(1)).publishEvent(any(ReviewCreatedEvent.class));
    }

    @Test
    @DisplayName("실패: 이미 리뷰를 작성한 커피챗에 다시 작성 시도 시 예외가 발생한다.")
    void createReview_Failure_AlreadyExists() {
        // given
        ReviewRequestDto requestDto = createReviewRequestDto(completedChat.getId(), 5, "또 쓸래요");
        when(reviewRepository.existsByCoffeeChatApplicationId(completedChat.getId())).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> reviewService.createReview(requestDto, mentee.getId()))
                .isInstanceOf(BusinessException.class)
                .hasMessage("리뷰는 한 번만 작성할 수 있습니다.");
    }

    @Test
    @DisplayName("실패: 완료되지 않은 커피챗에 리뷰 작성 시도 시 예외가 발생한다.")
    void createReview_Failure_InvalidStatus() {
        // given
        // 테스트 시나리오에 맞게 'ACCEPTED' 상태의 객체를 새로 생성
        CoffeeChatApplication acceptedChat = CoffeeChatApplication.builder().status(CoffeeChatApplication.Status.ACCEPTED).build();
        ReviewRequestDto requestDto = createReviewRequestDto(acceptedChat.getId(), 5, "미리 쓸래요");
        when(reviewRepository.existsByCoffeeChatApplicationId(acceptedChat.getId())).thenReturn(false);
        when(coffeeChatApplicationRepository.findById(acceptedChat.getId())).thenReturn(Optional.of(acceptedChat));

        // when & then
        assertThatThrownBy(() -> reviewService.createReview(requestDto, mentee.getId()))
                .isInstanceOf(BusinessException.class)
                .hasMessage("완료된 커피챗에만 리뷰를 작성할 수 있습니다.");
    }

    @Test
    @DisplayName("실패: 권한이 없는 사용자(멘토 등)가 리뷰 작성 시도 시 예외가 발생한다.")
    void createReview_Failure_NoAuthority() {
        // given
        Long otherUserId = 99L; // 다른 사용자 ID
        ReviewRequestDto requestDto = createReviewRequestDto(completedChat.getId(), 5, "내가 쓸래요");
        when(reviewRepository.existsByCoffeeChatApplicationId(completedChat.getId())).thenReturn(false);
        when(coffeeChatApplicationRepository.findById(completedChat.getId())).thenReturn(Optional.of(completedChat));

        // when & then
        assertThatThrownBy(() -> reviewService.createReview(requestDto, otherUserId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("리뷰를 작성할 권한이 없습니다.");
    }

    // 테스트용 DTO 생성을 위한 헬퍼 메서드
    private ReviewRequestDto createReviewRequestDto(Long chatId, int rating, String comment) {
        ReviewRequestDto dto = new ReviewRequestDto();
        ReflectionTestUtils.setField(dto, "chatId", chatId);
        ReflectionTestUtils.setField(dto, "rating", rating);
        ReflectionTestUtils.setField(dto, "comment", comment);
        return dto;
    }
}