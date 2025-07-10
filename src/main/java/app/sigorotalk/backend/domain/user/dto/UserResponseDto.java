package app.sigorotalk.backend.domain.user.dto;

import app.sigorotalk.backend.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
    private final Long userId;
    private final String email;
    private final String name;

    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
