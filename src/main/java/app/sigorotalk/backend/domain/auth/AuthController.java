package app.sigorotalk.backend.domain.auth;


import app.sigorotalk.backend.common.response.ApiResponse;
import app.sigorotalk.backend.domain.auth.dto.LoginRequestDto;
import app.sigorotalk.backend.domain.auth.dto.LoginResponseDto;
import app.sigorotalk.backend.domain.auth.dto.TokenRefreshResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        LoginResponseDto loginResponse = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword(), response);

        return ResponseEntity.ok(ApiResponse.success(loginResponse));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenRefreshResponseDto>> refresh(@CookieValue(name = "refresh_token") String refreshToken) {
        TokenRefreshResponseDto tokenRefreshResponseDto = authService.refresh(refreshToken);
        return ResponseEntity.ok(ApiResponse.success(tokenRefreshResponseDto));
    }
}