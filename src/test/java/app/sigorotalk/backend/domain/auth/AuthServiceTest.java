//package app.sigorotalk.backend.domain.auth;
//
//import app.sigorotalk.backend.common.exception.BusinessException;
//import app.sigorotalk.backend.config.jwt.JwtTokenProvider;
//import app.sigorotalk.backend.domain.auth.dto.LoginResponseDto;
//import app.sigorotalk.backend.domain.auth.dto.TokenRefreshResponseDto;
//import app.sigorotalk.backend.domain.user.User;
//import app.sigorotalk.backend.domain.user.UserRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//
//import java.util.Collections;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//
//@ExtendWith(MockitoExtension.class)
//class AuthServiceTest {
//
//    @InjectMocks
//    private AuthService authService;
//
//    @Mock
//    private JwtTokenProvider jwtTokenProvider;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private AuthenticationManagerBuilder authenticationManagerBuilder;
//
//    @Test
//    @DisplayName("재발급 성공: 유효한 Refresh Token으로 새 Access Token을 발급받는다.")
//    void refresh_Success() {
//        // given
//        String refreshToken = "valid-refresh-token";
//        String newAccessToken = "new-access-token";
//        String userId = "1";
//        User testUser = User.builder()
//                .id(1L)
//                .refreshToken(refreshToken)
//                .build();
//        Authentication authentication = createTestAuthentication(userId);
//
//        when(jwtTokenProvider.validateToken(refreshToken)).thenReturn(true);
//        when(jwtTokenProvider.getAuthentication(refreshToken)).thenReturn(authentication);
//        when(userRepository.findById(Long.valueOf(userId))).thenReturn(Optional.of(testUser));
//        when(jwtTokenProvider.createAccessToken(authentication)).thenReturn(newAccessToken);
//
//        // when
//        TokenRefreshResponseDto responseDto = authService.refresh(refreshToken);
//
//        // then
//        assertThat(responseDto.accessToken()).isEqualTo(newAccessToken);
//    }
//
//    @Test
//    @DisplayName("재발급 실패: 만료되었거나 위조된 Refresh Token은 BusinessException을 발생시킨다.")
//    void refresh_Failure_InvalidToken() {
//        // given
//        String invalidRefreshToken = "invalid-token";
//        when(jwtTokenProvider.validateToken(invalidRefreshToken)).thenReturn(false);
//
//        // when & then
//        assertThatThrownBy(() -> authService.refresh(invalidRefreshToken))
//                .isInstanceOf(BusinessException.class)
//                .satisfies(e -> assertThat(((BusinessException) e).getCode()).isEqualTo(AuthErrorCode.INVALID_REFRESH_TOKEN.getCode()));
//    }
//
//    @Test
//    @DisplayName("재발급 실패: DB에 저장된 토큰과 일치하지 않으면 BusinessException을 발생시킨다.")
//    void refresh_Failure_TokenNotMatch() {
//        // given
//        String requestToken = "request-token-from-client";
//        String dbToken = "different-token-in-db";
//        String userId = "1";
//        User testUser = User.builder().id(1L).refreshToken(dbToken).build();
//        Authentication authentication = createTestAuthentication(userId);
//
//        when(jwtTokenProvider.validateToken(requestToken)).thenReturn(true);
//        when(jwtTokenProvider.getAuthentication(requestToken)).thenReturn(authentication);
//        when(userRepository.findById(Long.valueOf(userId))).thenReturn(Optional.of(testUser));
//
//        // when & then
//        assertThatThrownBy(() -> authService.refresh(requestToken))
//                .isInstanceOf(BusinessException.class)
//                .satisfies(e -> assertThat(((BusinessException) e).getCode()).isEqualTo(AuthErrorCode.REFRESH_TOKEN_NOT_MATCH.getCode()));
//    }
//
//    @Test
//    @DisplayName("로그인 성공: 올바른 정보로 로그인 시, DTO 반환 및 쿠키를 설정한다.")
//    void login_Success() {
//        // given
//        String email = "test@example.com";
//        String password = "password";
//        String accessToken = "test-access-token";
//        String refreshToken = "test-refresh-token";
//
//        User testUser = User.builder()
//                .email(email)
//                .password("some-password")
//                .name("Test User")
//                .userType(User.UserType.BUYER)
//                .build();
//
//        Authentication authentication = createTestAuthentication("1"); // 헬퍼 메서드 사용
//        MockHttpServletResponse response = new MockHttpServletResponse(); // 가짜 HttpServletResponse
//
//        // Mock 객체들의 행동 정의
//        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager -> authentication);
//        when(jwtTokenProvider.createAccessToken(any(Authentication.class))).thenReturn(accessToken);
//        when(jwtTokenProvider.createRefreshToken(any(Authentication.class))).thenReturn(refreshToken);
//        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
//
//        // when
//        LoginResponseDto resultDto = authService.login(email, password, response);
//
//        // then
//        // 1. 반환된 DTO 검증
//        assertThat(resultDto.getAccessToken()).isEqualTo(accessToken);
//
//        // 2. Refresh Token이 User 객체에 잘 업데이트 되었는지 확인
//        assertThat(testUser.getRefreshToken()).isEqualTo(refreshToken);
//
//        // 3. 응답 헤더에 Set-Cookie가 잘 설정되었는지 검증
//        String cookieHeader = response.getHeader("Set-Cookie");
//        assertThat(cookieHeader).isNotNull();
//        assertThat(cookieHeader).contains("refresh_token=" + refreshToken);
//        assertThat(cookieHeader).contains("HttpOnly");
//    }
//
//    // 테스트용 Authentication 객체를 쉽게 만들기 위한 헬퍼 메서드
//    private Authentication createTestAuthentication(String userId) {
//        return new UsernamePasswordAuthenticationToken(userId, "", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
//    }
//}