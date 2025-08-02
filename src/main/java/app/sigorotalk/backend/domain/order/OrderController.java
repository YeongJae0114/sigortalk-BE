package app.sigorotalk.backend.domain.order;


import app.sigorotalk.backend.common.response.ApiResponse;
import app.sigorotalk.backend.domain.order.dto.OrderRequestDto;
import app.sigorotalk.backend.domain.order.dto.OrderResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDto>> orderProduct(
            @Valid @RequestBody OrderRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        // 1. 현재 로그인한 사용자의 정보를 SecurityContext에서 가져옴
        // (CustomUserDetailService에서 Principal을 userId로 설정했음)
        Long userId = Long.parseLong(userDetails.getUsername());

        // 2. 서비스 로직 호출하여 주문 처리
        OrderResponseDto responseDto = orderService.processOrder(userId, requestDto);

        // 3. 성공 응답 반환 (201 Created)
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(responseDto));
    }
}
