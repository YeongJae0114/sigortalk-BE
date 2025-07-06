package app.sigorotalk.backend.domain.auth;


import app.sigorotalk.backend.common.response.ApiResponse;
import app.sigorotalk.backend.config.jwt.JwtAuthenticationFilter;
import app.sigorotalk.backend.domain.auth.dto.LoginRequestDto;
import app.sigorotalk.backend.domain.auth.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ApiResponse<TokenDto>> login(@RequestBody LoginRequestDto loginRequestDto) {
        String accessToken = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        // 헤더에도 토큰 추가
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtAuthenticationFilter.AUTHORIZATION_HEADER, "Bearer " + accessToken);

        return new ResponseEntity<>(
                ApiResponse.success(new TokenDto(accessToken)),
                httpHeaders,
                HttpStatus.OK
        );
    }
}