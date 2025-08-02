package app.sigorotalk.backend.domain.farm_project.dto;

import app.sigorotalk.backend.domain.farm_project.FarmProject;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class LatestProjectResponseDto {

    private final Long projectId;
    private final String projectTitle;
    private final FarmerInfo farmer;
    private final BigDecimal price; // 별점, 후기 필드 삭제

    @Getter
    @Builder
    public static class FarmerInfo {
        private final String name;
        private final String location;
        private final String specialNote; // 'cultivationMethod'를 특이사항으로 사용
    }

    // from 정적 메소드를 더 간결하게 수정
    public static LatestProjectResponseDto from(FarmProject project) {
        // 프로젝트의 첫 번째 상품이 존재하면 그 가격을, 없으면 null을 반환
        BigDecimal price = project.getProducts().isEmpty() ? null : project.getProducts().get(0).getPrice();

        return LatestProjectResponseDto.builder()
                .projectId(project.getId())
                .projectTitle(project.getName())
                .farmer(FarmerInfo.builder()
                        .name(project.getFarmer().getUser().getName())
                        .location(project.getFarmer().getFarmLocation())
                        .specialNote(project.getFarmer().getCultivationMethod())
                        .build())
                .price(price) // 별점, 후기 관련 로직 삭제
                .build();
    }
}