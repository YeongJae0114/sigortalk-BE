package app.sigorotalk.backend.domain.mentor;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 멘토 관련 비동기 작업을 처리하는 서비스
 */
@Service
@RequiredArgsConstructor
public class MentorAsyncService {

    private final MentorRatingUpdater mentorRatingUpdater;

    @Async // 이 메서드는 호출 즉시 별도의 스레드에서 실행됩니다.
    public void updateMentorRating(Long mentorId) {
        mentorRatingUpdater.update(mentorId);
    }
}