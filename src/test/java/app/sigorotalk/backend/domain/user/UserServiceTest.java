package app.sigorotalk.backend.domain.user;

import app.sigorotalk.backend.common.exception.BusinessException;
import app.sigorotalk.backend.domain.user.dto.SignUpRequestDto;
import app.sigorotalk.backend.domain.user.dto.UserResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 성공: 중복되지 않은 이메일로 회원가입 시, 성공적으로 사용자를 저장한다.")
    void signup_Success() {
        // given
        // 헬퍼 메소드를 사용하여 DTO 생성
        SignUpRequestDto requestDto = createSignUpRequestDto("newuser@example.com", "password123", "새싹");
        User userEntity = requestDto.toEntity(passwordEncoder);

        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(userEntity);

        // when
        UserResponseDto responseDto = userService.signup(requestDto);

        // then
        assertThat(responseDto.getEmail()).isEqualTo(requestDto.getEmail());
        assertThat(responseDto.getName()).isEqualTo(requestDto.getName());
    }

    @Test
    @DisplayName("회원가입 실패: 이미 존재하는 이메일로 가입 시, BusinessException을 발생시킨다.")
    void signup_Failure_EmailAlreadyExists() {
        // given
        SignUpRequestDto requestDto = createSignUpRequestDto("exists@example.com", "password123", "중복");

        // [문제 2 해결책]
        // new User() 대신, 빌더를 사용하여 유효한 User 객체를 생성합니다.
        // 테스트 목적상 실제 값이 중요하지 않으므로 최소한의 정보만으로 만듭니다.
        User existingUser = User.builder()
                .email("exists@example.com")
                .password("some-encoded-password")
                .name("중복")
                .build();

        // userRepository.findByEmail이 호출되면, 위에서 만든 User 객체를 포함한 Optional을 반환하도록 설정
        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.of(existingUser));

        // when & then
        assertThatThrownBy(() -> userService.signup(requestDto))
                .isInstanceOf(BusinessException.class)
                .hasMessage("이미 존재하는 이메일입니다.");
    }

    // SignUpRequestDto 생성을 위한 private 헬퍼 메소드
    private SignUpRequestDto createSignUpRequestDto(String email, String password, String name) {
        SignUpRequestDto dto = new SignUpRequestDto();
        // ReflectionTestUtils를 사용해 DTO의 private final 필드에 값을 설정
        ReflectionTestUtils.setField(dto, "email", email);
        ReflectionTestUtils.setField(dto, "password", password);
        ReflectionTestUtils.setField(dto, "name", name);
        return dto;
    }
}