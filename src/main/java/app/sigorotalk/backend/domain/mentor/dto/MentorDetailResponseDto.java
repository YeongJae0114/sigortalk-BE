package app.sigorotalk.backend.domain.mentor.dto;

import app.sigorotalk.backend.domain.mentor.Mentor;
import app.sigorotalk.backend.domain.review.Review;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class MentorDetailResponseDto {

    private final Long mentorId;
    private final String name;
    private final String profileImageUrl;
    private final String expertise;
    private final String region;
    private final String shortDescription;
    private final String detailedDescription;
    private final String career;
    private final BigDecimal averageRating;
    private final List<ReviewDto> reviews;

    public static MentorDetailResponseDto from(Mentor mentor, List<Review> reviews) {
        return MentorDetailResponseDto.builder()
                .mentorId(mentor.getId())
                .name(mentor.getUser().getName())
                .profileImageUrl(mentor.getProfileImageUrl())
                .expertise(mentor.getExpertise())
                .region(mentor.getRegion())
                .shortDescription(mentor.getShortDescription())
                .detailedDescription(mentor.getDetailedDescription())
                .career(mentor.getCareer())
                .averageRating(mentor.getAverageRating())
                .reviews(reviews.stream().map(ReviewDto::from).collect(Collectors.toList()))
                .build();
    }

    @Getter
    @Builder
    private static class ReviewDto { // private record -> private static class
        private final Integer rating;
        private final String comment;

        static ReviewDto from(Review review) {
            return ReviewDto.builder()
                    .rating(review.getRating())
                    .comment(review.getComment())
                    .build();
        }
    }
}