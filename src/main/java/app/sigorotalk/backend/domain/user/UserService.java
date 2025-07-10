package app.sigorotalk.backend.domain.user;

import app.sigorotalk.backend.common.exception.BusinessException;
import app.sigorotalk.backend.domain.user.dto.SignUpRequestDto;
import app.sigorotalk.backend.domain.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto signup(SignUpRequestDto requestDto) {
        // 1. 이메일 중복 검사
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new BusinessException(UserErrorCode.EMAIL_ALREADY_EXISTS); // 중복 시 예외 발생
        }

        // 2. 사용자 생성 및 비밀번호 암호화
        User newUser = requestDto.toEntity(passwordEncoder);

        // 3. 사용자 저장
        User savedUser = userRepository.save(newUser);

        // 4. 응답 DTO로 변환하여 반환
        return UserResponseDto.from(savedUser);
    }

}
