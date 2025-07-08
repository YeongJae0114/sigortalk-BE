package app.sigorotalk.backend.domain.user.dto;

import app.sigorotalk.backend.domain.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
public class SignUpRequestDto {

    @Email(message = "유효한 이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;

    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(this.email)
                .password(passwordEncoder.encode(this.password)) // 암호화 로직 포함
                .name(this.name)
                .role(User.Role.ROLE_USER)
                .build();
    }
}
