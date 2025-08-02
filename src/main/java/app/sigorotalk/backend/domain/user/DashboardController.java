package app.sigorotalk.backend.domain.user;

import app.sigorotalk.backend.common.response.ApiResponse;
import app.sigorotalk.backend.domain.user.dto.MyDashboardResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MyDashboardResponseDto>> getMyDashboard(
            @AuthenticationPrincipal UserDetails userDetails) {

        // Spring Security 컨텍스트에서 현재 로그인한 사용자의 ID를 가져옴
        Long userId = Long.parseLong(userDetails.getUsername());

        // 서비스 로직을 호출하여 대시보드 정보를 가져옴
        MyDashboardResponseDto dashboardInfo = dashboardService.getDashboardInfo(userId);

        // 성공 응답 반환
        return ResponseEntity.ok(ApiResponse.success(dashboardInfo));
    }
}