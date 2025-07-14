package app.sigorotalk.backend.domain.mentor;

import app.sigorotalk.backend.common.exception.BusinessException;
import app.sigorotalk.backend.domain.review.Review;
import app.sigorotalk.backend.domain.review.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MentorRatingUpdaterTest {

    @InjectMocks
    private MentorRatingUpdater mentorRatingUpdater;

    @Mock
    private MentorRepository mentorRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Test
    @DisplayName("성공: update 메서드는 리뷰 목록을 기반으로 정확한 평점과 리뷰 수를 계산하여 멘토 정보를 업데이트한다.")
    void update_Success() {
        // given
        Long mentorId = 1L;
        Mentor mockMentor = mock(Mentor.class);
        List<Review> reviews = List.of(
                Review.builder().rating(5).build(),
                Review.builder().rating(4).build()
        );

        when(reviewRepository.findByCoffeeChatApplicationMentorId(mentorId)).thenReturn(reviews);
        when(mentorRepository.findById(mentorId)).thenReturn(Optional.of(mockMentor));

        // when
        mentorRatingUpdater.update(mentorId);

        // then
        verify(mockMentor, times(1)).updateRating(new BigDecimal("4.5"), 2);
    }

    @Test
    @DisplayName("성공(엣지 케이스): 멘토에 대한 리뷰가 하나도 없을 경우, 아무런 업데이트도 호출하지 않고 정상 종료된다.")
    void update_Success_When_NoReviews() {
        // given
        Long mentorId = 1L;
        // 리뷰가 없으므로 빈 리스트를 반환하도록 설정
        when(reviewRepository.findByCoffeeChatApplicationMentorId(mentorId)).thenReturn(Collections.emptyList());

        // when
        mentorRatingUpdater.update(mentorId);

        // then
        // mentorRepository.findById나 mentor.updateRating이 전혀 호출되지 않았음을 검증
        verify(mentorRepository, never()).findById(any());
        // 이 테스트에서는 mockMentor 객체를 만들 필요도, 사용할 일도 없습니다.
    }

    @Test
    @DisplayName("실패: 평점을 업데이트할 멘토를 찾지 못했을 경우, BusinessException을 던진다.")
    void update_Failure_When_MentorNotFound() {
        // given
        Long mentorId = 999L; // 존재하지 않는 멘토 ID
        List<Review> reviews = List.of(Review.builder().rating(5).build());

        when(reviewRepository.findByCoffeeChatApplicationMentorId(mentorId)).thenReturn(reviews);
        // findById가 비어있는 Optional을 반환하도록 설정
        when(mentorRepository.findById(mentorId)).thenReturn(Optional.empty());

        // when & then
        // mentorRatingUpdater.update(mentorId)를 실행하면 BusinessException이 발생하는지 검증
        assertThatThrownBy(() -> mentorRatingUpdater.update(mentorId))
                .isInstanceOf(BusinessException.class);
    }
}