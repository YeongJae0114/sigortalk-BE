package app.sigorotalk.backend.domain.mentor;

import app.sigorotalk.backend.common.exception.BusinessException;
import app.sigorotalk.backend.domain.review.Review;
import app.sigorotalk.backend.domain.review.ReviewCreatedEvent;
import app.sigorotalk.backend.domain.review.ReviewRepository;
import app.sigorotalk.backend.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MentorServiceTest {

    @InjectMocks
    private MentorService mentorService;

    @Mock
    private MentorRepository mentorRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MentorAsyncService mentorAsyncService;

    @Test
    @DisplayName("멘토 목록 조회 성공: 페이징된 멘토 목록을 DTO로 변환하여 반환한다.")
    void getMentorList_Success() {
        // given
        User testUser = User.builder().name("테스트 멘토").build();
        Mentor testMentor = Mentor.builder().user(testUser).build();
        Page<Mentor> mentorPage = new PageImpl<>(Collections.singletonList(testMentor));
        Pageable pageable = PageRequest.of(0, 10);

        // 필터링 조건이 없는 경우를 테스트하기 위해 region과 expertise에 null을 전달합니다.
        when(mentorRepository.findByFilters(null, null, pageable)).thenReturn(mentorPage);

        var result = mentorService.getMentorList(null, null, pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("테스트 멘토");
    }

    @Test
    @DisplayName("멘토 목록 조회 성공: 필터링 조건이 있을 경우, 조건에 맞는 목록을 반환한다.")
    void getMentorList_WithFilters_Success() {
        // given
        String region = "서울";
        String expertise = "IT";
        Pageable pageable = PageRequest.of(0, 10);

        // 서울, IT 전문가인 멘토 1명만 포함된 페이지를 반환하도록 설정
        User testUser = User.builder().name("서울 IT 멘토").build();
        Mentor testMentor = Mentor.builder().user(testUser).region(region).expertise(expertise).build();
        Page<Mentor> filteredPage = new PageImpl<>(Collections.singletonList(testMentor));

        when(mentorRepository.findByFilters(region, expertise, pageable)).thenReturn(filteredPage);

        // when
        var result = mentorService.getMentorList(region, expertise, pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("서울 IT 멘토");
    }

    @Test
    @DisplayName("멘토 상세 조회 성공: 멘토 정보와 리뷰 목록을 포함한 DTO를 반환한다.")
    void getMentorDetail_Success() {
        // given
        User testUser = User.builder().name("상세조회 멘토").build();
        Mentor testMentor = Mentor.builder().id(1L).user(testUser).build();
        List<Review> reviews = Collections.emptyList();

        when(mentorRepository.findByIdWithUser(1L)).thenReturn(Optional.of(testMentor));
        when(reviewRepository.findByCoffeeChatApplicationMentorId(1L)).thenReturn(reviews);

        // when
        var result = mentorService.getMentorDetail(1L);

        // then
        assertThat(result.getMentorId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("상세조회 멘토");
        assertThat(result.getReviews()).isEmpty();
    }

    @Test
    @DisplayName("멘토 상세 조회 실패: 존재하지 않는 ID로 조회 시 BusinessException 발생")
    void getMentorDetail_Failure_NotFound() {
        // given
        when(mentorRepository.findByIdWithUser(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> mentorService.getMentorDetail(999L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("요청한 리소스를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("성공: ReviewCreatedEvent가 발생하면, MentorAsyncService를 통해 평점 업데이트를 요청한다.")
    void handleReviewCreatedEvent_Should_Call_AsyncService() {
        // given
        Long mentorId = 1L;
        ReviewCreatedEvent event = new ReviewCreatedEvent(mentorId);

        // when
        mentorService.handleReviewCreatedEvent(event);

        // then
        // mentorAsyncService.updateMentorRating(mentorId)가 1번 호출되었는지 검증
        verify(mentorAsyncService, times(1)).updateMentorRating(mentorId);
    }
}