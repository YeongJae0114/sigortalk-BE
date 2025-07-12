package app.sigorotalk.backend.domain.coffeechat.dto;

import app.sigorotalk.backend.domain.coffeechat.CoffeeChatApplication;

public record ChatStatusUpdateResponseDto(Long applicationId, String updatedStatus) {
    public static ChatStatusUpdateResponseDto from(CoffeeChatApplication application) {
        return new ChatStatusUpdateResponseDto(application.getId(), application.getStatus().name());
    }
}