package app.sigorotalk.backend.config.jwt;

import io.jsonwebtoken.security.SecurityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtTokenProviderTest {

    // 테스트용 설정값
    private final String testSecret = "V2VMa2VTaWdvcm90YWxrQW5kSXRzQmVzdG9mQmVzdFNlY3JldEtleUZvclByb2plY3Q=";
    // 테스트 대상 클래스
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        // 각 테스트가 실행되기 전에 JwtTokenProvider 인스턴스를 새로 생성
        long accessTokenValidityInSeconds = 3600;   // 1시간
        jwtTokenProvider = new JwtTokenProvider(testSecret, accessTokenValidityInSeconds);
    }

    @Test
    @DisplayName("토큰 생성 및 검증 성공: 유효한 Authentication 객체로 토큰을 정상적으로 생성한다.")
    void createAndValidateToken_Success() {
        // given: 테스트 준비
        // "1" 이라는 ID와 "ROLE_USER" 권한을 가진 사용자 인증 정보 생성
        Authentication authentication = createTestAuthentication("1", "ROLE_USER");

        // when: 테스트 실행
        // 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(authentication);

        // then: 결과 검증
        assertThat(accessToken).isNotNull(); // 토큰이 null이 아니어야 함

        // 생성된 토큰이 유효한지 검증
        boolean isValid = jwtTokenProvider.validateToken(accessToken);
        assertThat(isValid).isTrue(); // 토큰은 유효해야 함
    }

    @Test
    @DisplayName("토큰 해석 성공: 생성된 토큰에서 올바른 Authentication 객체를 가져온다.")
    void getAuthentication_Success() {
        // given: 테스트 준비
        String userId = "100";
        String userRole = "ROLE_ADMIN";
        Authentication originalAuth = createTestAuthentication(userId, userRole);
        String accessToken = jwtTokenProvider.createAccessToken(originalAuth);

        // when: 테스트 실행
        // 토큰을 다시 Authentication 객체로 변환
        Authentication resultAuth = jwtTokenProvider.getAuthentication(accessToken);

        // then: 결과 검증
        assertThat(resultAuth.getName()).isEqualTo(userId); // 사용자 ID가 일치해야 함
        assertThat(resultAuth.getAuthorities()).hasSize(1); // 권한이 1개여야 함
        assertThat(resultAuth.getAuthorities().iterator().next().getAuthority()).isEqualTo(userRole); // 권한 이름이 일치해야 함
    }

    @Test
    @DisplayName("토큰 유효성 검증 실패: 만료된 토큰은 false를 반환한다.")
    void validateToken_Failure_Expired() {
        // given: 테스트 준비
        // 만료 시간을 아주 짧게 설정한 토큰 프로바이더를 새로 생성
        JwtTokenProvider expiredTokenProvider = new JwtTokenProvider(testSecret, 0); // 유효시간 0초
        Authentication authentication = createTestAuthentication("1", "ROLE_USER");
        String expiredToken = expiredTokenProvider.createAccessToken(authentication);

        // when: 테스트 실행
        // 만료된 토큰 검증
        boolean isValid = jwtTokenProvider.validateToken(expiredToken);

        // then: 결과 검증
        assertThat(isValid).isFalse(); // 토큰은 유효하지 않아야 함
    }

    @Test
    @DisplayName("토큰 유효성 검증 실패: 서명이 위조된 토큰은 false를 반환한다.")
    void validateToken_Failure_InvalidSignature() {
        // given: 테스트 준비
        // 원본 토큰 생성
        Authentication authentication = createTestAuthentication("1", "ROLE_USER");
        String originalToken = jwtTokenProvider.createAccessToken(authentication);

        // 토큰의 마지막 글자를 하나 변경하여 서명을 위조
        String tamperedToken = originalToken.substring(0, originalToken.length() - 1) + "X";

        // when: 테스트 실행
        boolean isValid = jwtTokenProvider.validateToken(tamperedToken);

        // then: 결과 검증
        assertThat(isValid).isFalse(); // 토큰은 유효하지 않아야 함
    }

    @Test
    @DisplayName("토큰 유효성 검증 실패: 지원되지 않는 토큰은 false를 반환한다.")
    void validateToken_Failure_Unsupported() {
        // given: 테스트 준비
        // 헤더나 페이로드 없이 서명만 있는 의미 없는 토큰
        String unsupportedToken = "this.is.unsupported";

        // when: 테스트 실행
        boolean isValid = jwtTokenProvider.validateToken(unsupportedToken);

        // then: 결과 검증
        assertThat(isValid).isFalse(); // 유효하지 않아야 함
    }

    @Test
    @DisplayName("토큰 해석 실패: 서명이 위조된 토큰은 SecurityException을 발생시킨다.")
    void getAuthentication_Failure_InvalidSignature() {
        // given
        String originalToken = jwtTokenProvider.createAccessToken(createTestAuthentication("1", "ROLE_USER"));
        String tamperedToken = originalToken.substring(0, originalToken.length() - 1) + "X";

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.getAuthentication(tamperedToken))
                .isInstanceOf(SecurityException.class);
    }

    @Test
    @DisplayName("토큰 유효성 검증 실패: 다른 비밀키로 서명된 토큰은 false를 반환한다.")
    void validateToken_Failure_WrongSecretKey() {
        // given
        String wrongSecret = "ThisIsTotallyDifferentAndWrongSecretKeyForTestingPurposesWowGreat";
        JwtTokenProvider wrongProvider = new JwtTokenProvider(wrongSecret, 3600);
        String tokenSignedWithWrongKey = wrongProvider.createAccessToken(createTestAuthentication("1", "ROLE_USER"));

        // when
        boolean isValid = jwtTokenProvider.validateToken(tokenSignedWithWrongKey);

        // then
        assertThat(isValid).isFalse();
    }

    // 테스트용 Authentication 객체를 쉽게 만들기 위한 헬퍼 메서드
    private Authentication createTestAuthentication(String userId, String role) {
        return new UsernamePasswordAuthenticationToken(
                userId,
                null,
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }
}