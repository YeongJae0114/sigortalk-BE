package app.sigorotalk.backend.domain.user;


import app.sigorotalk.backend.common.response.ApiResponse;
import app.sigorotalk.backend.domain.user.dto.SignUpRequestDto;
import app.sigorotalk.backend.domain.user.dto.UserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> signup(@Valid @RequestBody SignUpRequestDto requestDto) {
        UserResponseDto responseDto = userService.signup(requestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED) // 201 Created
                .body(ApiResponse.success(responseDto));
    }

    // --- 테스트용 엔드포인트 추가 ---
    @GetMapping("/me")
    public ResponseEntity<String> getMyInfo() {
        // 실제로는 SecurityContext에서 사용자 정보를 꺼내 반환해야 합니다.
        // 지금은 통합 테스트를 위해 간단히 "ok"만 반환하도록 만듭니다.
        return ResponseEntity.ok("ok");
    }
}
