package app.sigorotalk.backend.domain.review;


import app.sigorotalk.backend.common.response.ApiResponse;
import app.sigorotalk.backend.domain.review.dto.ReviewRequestDto;
import app.sigorotalk.backend.domain.review.dto.ReviewResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponseDto>> createReview(
            @Valid @RequestBody ReviewRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long menteeId = Long.valueOf(userDetails.getUsername());
        ReviewResponseDto responseDto = reviewService.createReview(requestDto, menteeId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(responseDto));
    }
}
