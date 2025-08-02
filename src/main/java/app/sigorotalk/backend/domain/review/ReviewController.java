package app.sigorotalk.backend.domain.review;

import app.sigorotalk.backend.common.response.ApiResponse;
import app.sigorotalk.backend.domain.review.dto.ReviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/projects/{projectId}/reviews")
    public ResponseEntity<ApiResponse<List<ReviewResponseDto>>> getProjectReviews(
            @PathVariable Long projectId) {

        List<ReviewResponseDto> reviews = reviewService.getReviewsForProject(projectId);
        return ResponseEntity.ok(ApiResponse.success(reviews));
    }

    @GetMapping("/reviews/latest")
    public ResponseEntity<ApiResponse<List<ReviewResponseDto>>> getLatestReviews() {
        List<ReviewResponseDto> latestReviews = reviewService.getLatest10Reviews();
        return ResponseEntity.ok(ApiResponse.success(latestReviews));
    }
}