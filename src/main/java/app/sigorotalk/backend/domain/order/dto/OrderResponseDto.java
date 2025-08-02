package app.sigorotalk.backend.domain.order.dto;

import lombok.Getter;

@Getter
public class OrderResponseDto {

    private final Long orderId;
    private final String orderStatus;
    private final String message;

    public OrderResponseDto(Long orderId, String orderStatus) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.message = "주문 및 결제가 성공적으로 완료되었습니다.";
    }
}