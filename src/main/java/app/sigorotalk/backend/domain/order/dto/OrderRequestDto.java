package app.sigorotalk.backend.domain.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OrderRequestDto {

    @NotNull(message = "상품 ID는 필수입니다.")
    private Long productId;

}
