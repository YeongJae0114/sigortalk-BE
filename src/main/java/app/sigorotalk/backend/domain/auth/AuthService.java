package app.sigorotalk.backend.domain.auth;

import app.sigorotalk.backend.common.exception.BusinessException;
import app.sigorotalk.backend.config.jwt.JwtTokenProvider;
import app.sigorotalk.backend.domain.auth.dto.LoginResponseDto;
import app.sigorotalk.backend.domain.auth.dto.TokenRefreshResponseDto;
import app.sigorotalk.backend.domain.user.User;
import app.sigorotalk.backend.domain.user.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일

    @Transactional
    public LoginResponseDto login(String email, String password, HttpServletResponse response) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        // 2. 실제 검증 (비밀번호 체크) -> CustomUserDetailService.loadUserByUsername 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);

        // 4. 이메일로 사용자 정보 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + " -> 데이터베이스에서 찾을 수 없습니다."));
        user.updateRefreshToken(refreshToken);

        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                .maxAge(REFRESH_TOKEN_EXPIRE_TIME / 1000) // 초 단위로 설정
                .path("/")
                .secure(true) // 배포 환경에서는 true로 설정
                .sameSite("None") // 필요에 따라 Strict 또는 Lax로 설정
                .httpOnly(true)
                .build();

        // 2. 응답 헤더에 쿠키 추가
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // 3. 수정된 DTO에 맞춰서 AccessToken과 유저 정보만 반환
        return LoginResponseDto.of(accessToken, user);
    }

    @Transactional
    public TokenRefreshResponseDto refresh(String refreshToken) {
        // 1. Refresh Token 유효성 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BusinessException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 2. Token에서 사용자 ID 추출
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        String userId = authentication.getName();

        // 3. DB의 Refresh Token과 대조
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.getRefreshToken().equals(refreshToken)) {
            throw new BusinessException(AuthErrorCode.REFRESH_TOKEN_NOT_MATCH);
        }

        // 4. 새로운 Access Token 생성
        String newAccessToken = jwtTokenProvider.createAccessToken(authentication);

        return new TokenRefreshResponseDto(newAccessToken);
    }
}