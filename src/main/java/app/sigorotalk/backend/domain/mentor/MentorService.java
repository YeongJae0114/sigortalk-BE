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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MentorService {

    private final MentorRepository mentorRepository;
    private final ReviewRepository reviewRepository;
    private final MentorAsyncService mentorAsyncService;

    public Page<MentorListResponseDto> getMentorList(String region, String expertise, Pageable pageable) {
        return mentorRepository.findByFilters(region, expertise, pageable)
                .map(MentorListResponseDto::from);
    }

    public MentorDetailResponseDto getMentorDetail(Long mentorId) {
        Mentor mentor = mentorRepository.findByIdWithUser(mentorId)
                .orElseThrow(() -> new BusinessException(CommonErrorCode.NOT_FOUND));
        List<Review> reviews = reviewRepository.findByCoffeeChatApplicationMentorId(mentorId);
        return MentorDetailResponseDto.from(mentor, reviews);
    }

    // TODO: (관리자용) 멘토 등록 로직 구현

    /**
     * ReviewCreatedEvent를 수신하여, 비동기 서비스에게 멘토 평점 업데이트를 요청합니다.
     */
    @EventListener
    public void handleReviewCreatedEvent(ReviewCreatedEvent event) {
        mentorAsyncService.updateMentorRating(event.getMentorId());
    }
}
