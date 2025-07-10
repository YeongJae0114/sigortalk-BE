package app.sigorotalk.backend.domain.mentor.dto;

import app.sigorotalk.backend.domain.mentor.Mentor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class MentorListResponseDto {
    private final Long mentorId;
    private final String name;
    private final String profileImageUrl;
    private final String expertise;
    private final String region;
    private final String shortDescription;
    private final BigDecimal averageRating;
    private final Integer reviewCount;

    public static MentorListResponseDto from(Mentor mentor) {
        return MentorListResponseDto.builder()
                .mentorId(mentor.getId())
                .name(mentor.getUser().getName()) // User 엔티티에서 이름 가져오기
                .profileImageUrl(mentor.getProfileImageUrl())
                .expertise(mentor.getExpertise())
                .region(mentor.getRegion())
                .shortDescription(mentor.getShortDescription())
                .averageRating(mentor.getAverageRating())
                .reviewCount(mentor.getReviewCount())
                .build();
    }
}