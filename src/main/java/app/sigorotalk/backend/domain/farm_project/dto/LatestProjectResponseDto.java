package app.sigorotalk.backend.domain.farm_project.dto;

import app.sigorotalk.backend.domain.farm_project.FarmProject;
import app.sigorotalk.backend.domain.product.Product;
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
    private final String productImageUrl;

    @Getter
    @Builder
    public static class FarmerInfo {
        private final String name;
        private final String location;
        private final String specialNote; // 'cultivationMethod'를 특이사항으로 사용
    }

    // from 정적 메소드를 더 간결하게 수정
    public static LatestProjectResponseDto from(FarmProject project) {
        // 첫 번째 상품이 있을 경우에만 가격과 이미지 URL을 설정
        BigDecimal price = null;
        String imageUrl = null;
        if (!project.getProducts().isEmpty()) {
            Product firstProduct = project.getProducts().get(0);
            price = firstProduct.getPrice();
            imageUrl = firstProduct.getImageUrl(); // 이미지 URL 가져오기
        }

        return LatestProjectResponseDto.builder()
                .projectId(project.getId())
                .projectTitle(project.getName())
                .farmer(FarmerInfo.builder()
                        .name(project.getFarmer().getUser().getName())
                        .location(project.getFarmer().getFarmLocation())
                        .specialNote(project.getFarmer().getCultivationMethod())
                        .build())
                .price(price)
                .productImageUrl(imageUrl) // 빌더에 이미지 URL 추가
                .build();
    }
}