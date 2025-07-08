package app.sigorotalk.backend.config.auth;

import app.sigorotalk.backend.common.exception.BusinessException;
import app.sigorotalk.backend.config.jwt.JwtTokenProvider;
import app.sigorotalk.backend.domain.auth.AuthErrorCode;
import app.sigorotalk.backend.domain.auth.AuthService;
import app.sigorotalk.backend.domain.auth.dto.TokenRefreshResponseDto;
import app.sigorotalk.backend.domain.user.User;
import app.sigorotalk.backend.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("재발급 성공: 유효한 Refresh Token으로 새 Access Token을 발급받는다.")
    void refresh_Success() {
        // given
        String refreshToken = "valid-refresh-token";
        String newAccessToken = "new-access-token";
        String userId = "1";
        User testUser = User.builder()
                .id(1L)
                .refreshToken(refreshToken)
                .build();
        Authentication authentication = createTestAuthentication(userId);

        when(jwtTokenProvider.validateToken(refreshToken)).thenReturn(true);
        when(jwtTokenProvider.getAuthentication(refreshToken)).thenReturn(authentication);
        when(userRepository.findById(Long.valueOf(userId))).thenReturn(Optional.of(testUser));
        when(jwtTokenProvider.createAccessToken(authentication)).thenReturn(newAccessToken);

        // when
        TokenRefreshResponseDto responseDto = authService.refresh(refreshToken);

        // then
        assertThat(responseDto.accessToken()).isEqualTo(newAccessToken);
    }

    @Test
    @DisplayName("재발급 실패: 만료되었거나 위조된 Refresh Token은 BusinessException을 발생시킨다.")
    void refresh_Failure_InvalidToken() {
        // given
        String invalidRefreshToken = "invalid-token";
        when(jwtTokenProvider.validateToken(invalidRefreshToken)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> authService.refresh(invalidRefreshToken))
                .isInstanceOf(BusinessException.class)
                .satisfies(e -> assertThat(((BusinessException) e).getCode()).isEqualTo(AuthErrorCode.INVALID_REFRESH_TOKEN.getCode()));
    }

    @Test
    @DisplayName("재발급 실패: DB에 저장된 토큰과 일치하지 않으면 BusinessException을 발생시킨다.")
    void refresh_Failure_TokenNotMatch() {
        // given
        String requestToken = "request-token-from-client";
        String dbToken = "different-token-in-db";
        String userId = "1";
        User testUser = User.builder().id(1L).refreshToken(dbToken).build();
        Authentication authentication = createTestAuthentication(userId);

        when(jwtTokenProvider.validateToken(requestToken)).thenReturn(true);
        when(jwtTokenProvider.getAuthentication(requestToken)).thenReturn(authentication);
        when(userRepository.findById(Long.valueOf(userId))).thenReturn(Optional.of(testUser));

        // when & then
        assertThatThrownBy(() -> authService.refresh(requestToken))
                .isInstanceOf(BusinessException.class)
                .satisfies(e -> assertThat(((BusinessException) e).getCode()).isEqualTo(AuthErrorCode.REFRESH_TOKEN_NOT_MATCH.getCode()));
    }

    // 테스트용 Authentication 객체를 쉽게 만들기 위한 헬퍼 메서드
    private Authentication createTestAuthentication(String userId) {
        return new UsernamePasswordAuthenticationToken(userId, "", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}