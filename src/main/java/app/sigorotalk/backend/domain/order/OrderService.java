package app.sigorotalk.backend.domain.order;

import app.sigorotalk.backend.common.exception.BusinessException;
import app.sigorotalk.backend.domain.order.dto.OrderRequestDto;
import app.sigorotalk.backend.domain.order.dto.OrderResponseDto;
import app.sigorotalk.backend.domain.payment.Payment;
import app.sigorotalk.backend.domain.product.Product;
import app.sigorotalk.backend.domain.product.ProductErrorCode;
import app.sigorotalk.backend.domain.product.ProductRepository;
import app.sigorotalk.backend.domain.user.User;
import app.sigorotalk.backend.domain.user.UserErrorCode;
import app.sigorotalk.backend.domain.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    /**
     * 주문 및 결제 처리 (트랜잭션으로 관리)
     *
     * @param userId     주문하는 사용자의 ID
     * @param requestDto 주문 요청 정보 (productId)
     * @return 주문 결과 정보
     */
    @Transactional
    public OrderResponseDto processOrder(Long userId, OrderRequestDto requestDto) {
        // 1. 사용자 및 상품 정보 조회
        // (상품은 동시성 제어를 위해 비관적 락을 걸어 안전하게 조회)
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        Product product = productRepository.findByIdWithPessimisticLock(requestDto.getProductId()).orElseThrow(() -> new BusinessException(ProductErrorCode.PRODUCT_NOT_FOUND));

        // 2. 주문 생성 (엔티티 내부의 생성 메서드 호출)
        Order order = Order.createOrder(user, product);

        // 3. 결제 정보 생성 (결제 시뮬레이션)
        Payment payment = new Payment();
        payment.setFinalAmount(order.getTotalPrice());
        payment.setMethod("SIMULATED_PAYMENT");
        order.setPayment(payment);

        // 4. 주문 저장 (Cascade 설정으로 OrderItem, Payment가 함께 저장됨)
        Order savedOrder = orderRepository.save(order);

        // 5. 결과 DTO 반환
        return new OrderResponseDto(savedOrder.getId(), savedOrder.getOrderStatus().name());
    }
}
