package app.sigorotalk.backend.domain.auth;


import app.sigorotalk.backend.common.response.ApiResponse;
import app.sigorotalk.backend.domain.auth.dto.LoginRequestDto;
import app.sigorotalk.backend.domain.auth.dto.LoginResponseDto;
import app.sigorotalk.backend.domain.auth.dto.RefreshRequestDto;
import app.sigorotalk.backend.domain.auth.dto.TokenRefreshResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponse = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        return ResponseEntity.ok(ApiResponse.success(loginResponse));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenRefreshResponseDto>> refresh(@RequestBody RefreshRequestDto requestDto) {
        TokenRefreshResponseDto responseDto = authService.refresh(requestDto.refreshToken());
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }
}