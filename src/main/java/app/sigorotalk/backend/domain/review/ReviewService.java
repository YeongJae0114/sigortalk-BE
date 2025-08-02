package app.sigorotalk.backend.domain.review;

import app.sigorotalk.backend.domain.farm_project.FarmProjectRepository;
import app.sigorotalk.backend.domain.review.dto.ReviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final FarmProjectRepository farmProjectRepository;

    public List<ReviewResponseDto> getReviewsForProject(Long projectId) {
        // 단 한 번의 쿼리로 프로젝트의 모든 리뷰와 관련 정보를 가져옴
        return reviewRepository.findAllByProjectIdWithDetails(projectId)
                .stream()
                .map(ReviewResponseDto::from)
                .collect(Collectors.toList());
    }

    public List<ReviewResponseDto> getLatest10Reviews() {
        return reviewRepository.findTop10ByOrderByCreatedAtDesc()
                .stream()
                .map(ReviewResponseDto::from)
                .collect(Collectors.toList());
    }
}