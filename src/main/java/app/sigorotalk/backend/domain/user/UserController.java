package app.sigorotalk.backend.domain.user;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    // TODO: [POST /] 회원가입 API (URL 경로가 클래스 레벨로 통합되었으므로 /signup 제거)
    // public ResponseEntity<?> signup( ... )


    // --- 테스트용 엔드포인트 추가 ---
    @GetMapping("/me")
    public ResponseEntity<String> getMyInfo() {
        // 실제로는 SecurityContext에서 사용자 정보를 꺼내 반환해야 합니다.
        // 지금은 통합 테스트를 위해 간단히 "ok"만 반환하도록 만듭니다.
        return ResponseEntity.ok("ok");
    }
}
