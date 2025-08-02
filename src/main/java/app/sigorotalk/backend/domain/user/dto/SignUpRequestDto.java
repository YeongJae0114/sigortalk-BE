package app.sigorotalk.backend.domain.user.dto;

import app.sigorotalk.backend.domain.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "핸드폰 번호 필수 입력값입니다.")
    private String phoneNumber;

    @NotNull(message = "사용자 유형(BUYER/FARMER)은 필수입니다.")
    private String userType; // 사용자가 선택한 유형을 받습니다.

    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(this.email)
                .password(passwordEncoder.encode(this.password))
                .phoneNumber(this.phoneNumber)
                .name(this.name)
                .userType(this.userType) // 전달받은 userType을 사용합니다.
                .build();
    }
}