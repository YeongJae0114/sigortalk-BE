package app.sigorotalk.backend.domain.farmer.dto;

import app.sigorotalk.backend.domain.farm_project.FarmProject;
import app.sigorotalk.backend.domain.farmer.Farmer;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class FarmerDetailResponseDto {

    private final Long farmerId;
    private final String name;
    private final String profileImageUrl; // 프로필 이미지 URL
    private final String farmLocation;
    private final String operationExperience;
    private final String deliverySystem;
    private final String cultivationMethod;
    private final List<FarmProjectDto> farmProjects; // 농장주가 진행하는 프로젝트 목록

    // Farmer 엔티티를 DTO로 변환하는 정적 메서드
    public static FarmerDetailResponseDto from(Farmer farmer) {
        return FarmerDetailResponseDto.builder()
                .farmerId(farmer.getId())
                .name(farmer.getUser().getName()) // User 엔티티에서 이름 가져오기
                .profileImageUrl(farmer.getProfile())
                .farmLocation(farmer.getFarmLocation())
                .operationExperience(farmer.getOperationExperience())
                .deliverySystem(farmer.getDeliverySystem())
                .cultivationMethod(farmer.getCultivationMethod())
                .farmProjects(
                        farmer.getFarmProjects().stream()
                                .map(FarmProjectDto::from)
                                .collect(Collectors.toList())
                )
                .build();
    }

    /**
     * 농장주의 프로젝트 정보를 담는 내부 DTO
     */
    @Getter
    @Builder
    private static class FarmProjectDto {
        private final Long projectId;
        private final String projectName;

        public static FarmProjectDto from(FarmProject farmProject) {
            return FarmProjectDto.builder()
                    .projectId(farmProject.getId())
                    .projectName(farmProject.getName())
                    .build();
        }
    }
}