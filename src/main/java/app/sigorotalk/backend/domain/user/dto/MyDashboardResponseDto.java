package app.sigorotalk.backend.domain.user.dto;

import app.sigorotalk.backend.domain.farm_project.FarmProject;
import app.sigorotalk.backend.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyDashboardResponseDto {

    private final String name;
    private final String email;
    private final List<DashboardProjectDto> myProjects; // 내가 참여한 모든 프로젝트 목록

    public static MyDashboardResponseDto of(User user, List<DashboardProjectDto> projects) {
        return MyDashboardResponseDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .myProjects(projects)
                .build();
    }

    /**
     * 대시보드에 포함될 프로젝트의 상세 정보를 담는 내부 DTO
     */
    @Getter
    @Builder
    public static class DashboardProjectDto {
        private final String projectTitle;
        private final String productImageUrl;
        private final FarmerInfo farmer;

        public static DashboardProjectDto from(FarmProject project) {
            // 프로젝트의 첫 번째 상품 이미지를 가져옴
            String imageUrl = project.getProducts().isEmpty() ? null : project.getProducts().get(0).getImageUrl();

            return DashboardProjectDto.builder()
                    .projectTitle(project.getName())
                    .productImageUrl(imageUrl)
                    .farmer(FarmerInfo.from(project.getFarmer()))
                    .build();
        }
    }

    /**
     * 농부의 상세 정보를 담는 내부 DTO
     */
    @Getter
    @Builder
    public static class FarmerInfo {
        private final String name;
        private final String location;
        private final String specialNote;

        public static FarmerInfo from(app.sigorotalk.backend.domain.farmer.Farmer farmer) {
            return FarmerInfo.builder()
                    .name(farmer.getUser().getName())
                    .location(farmer.getFarmLocation())
                    .specialNote(farmer.getCultivationMethod())
                    .build();
        }
    }
}