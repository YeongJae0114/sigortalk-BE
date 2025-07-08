package app.sigorotalk.backend.domain.auth.dto;

import app.sigorotalk.backend.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {

    private final String accessToken;
    private final UserInfo user;

    public static LoginResponseDto of(String accessToken, User userEntity) {
        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .user(UserInfo.builder()
                        .name(userEntity.getName())
                        .email(userEntity.getEmail())
                        .role(userEntity.getRole().name())
                        .build())
                .build();
    }

    @Getter
    @Builder
    public static class UserInfo {
        private final String name;
        private final String email;
        private final String role;
    }
}