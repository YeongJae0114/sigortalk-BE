package app.sigorotalk.backend.config.user;

import app.sigorotalk.backend.domain.user.CustomUserDetailService;
import app.sigorotalk.backend.domain.user.User;
import app.sigorotalk.backend.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // JUnit5에서 Mockito를 사용하기 위한 설정
class CustomUserDetailServiceTest {

    @Mock // 가짜(Mock) UserRepository 객체 생성
    private UserRepository userRepository;

    @InjectMocks // @Mock으로 만든 가짜 객체를 실제 CustomUserDetailService에 주입
    private CustomUserDetailService customUserDetailService;

    @Test
    @DisplayName("사용자 조회 성공: 존재하는 이메일로 조회 시 UserDetails 객체를 반환한다.")
    void loadUserByUsername_Success() {
        // given: 테스트 준비
        String userEmail = "test@example.com";
        // User 엔티티를 직접 생성하는 대신, 테스트용 User 객체를 반환하도록 설정
        User testUser = User.builder()
                .id(1L)
                .email(userEmail)
                .password("password123")
                .name("Test User")
                .role(User.Role.ROLE_USER)
                .build();
        // userRepository.findByEmail(userEmail)이 호출되면,
        // Optional.of(testUser)를 반환하라고 미리 약속(stub)합니다.
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(testUser));

        // when: 테스트 실행
        UserDetails userDetails = customUserDetailService.loadUserByUsername(userEmail);

        // then: 결과 검증
        assertThat(userDetails.getUsername()).isEqualTo("1"); // principal은 userId로 설정했으므로 "1"
        assertThat(userDetails.getPassword()).isEqualTo("password123");
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("사용자 조회 실패: 존재하지 않는 이메일로 조회 시 UsernameNotFoundException을 발생시킨다.")
    void loadUserByUsername_Failure_UserNotFound() {
        // given: 테스트 준비
        String nonExistentEmail = "notfound@example.com";

        // userRepository.findByEmail()이 어떤 문자열로 호출되든,
        // 항상 비어있는 Optional을 반환하라고 약속합니다.
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // when & then: 테스트 실행 및 결과 검증
        // 해당 이메일로 사용자를 찾을 수 없다는 예외가 발생하는지 확인합니다.
        assertThatThrownBy(() -> customUserDetailService.loadUserByUsername(nonExistentEmail))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage(nonExistentEmail + " -> 데이터베이스에서 찾을 수 없습니다.");
    }
}
