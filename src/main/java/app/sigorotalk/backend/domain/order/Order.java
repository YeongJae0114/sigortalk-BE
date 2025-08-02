package app.sigorotalk.backend.domain.order;
import app.sigorotalk.backend.common.entity.BaseTimeEntity;
import app.sigorotalk.backend.domain.order_item.OrderItem;
import app.sigorotalk.backend.domain.payment.Payment;
import app.sigorotalk.backend.domain.product.Product;
import app.sigorotalk.backend.domain.user.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 구매자

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status")
    private DeliveryStatus deliveryStatus;

    @Column(name = "shipped_at")
    private LocalDateTime shippedAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;

    public enum OrderStatus {
        PAYMENT_COMPLETED,  // 결제 완료
        ORDER_CANCELED      // 주문 취소
    }

    public enum DeliveryStatus {
        PREPARING,          // 배송 준비중
        SHIPPED,            // 배송중
        DELIVERED           // 배송 완료
    }

    //== 생성 메서드 ==//
    public static Order createOrder(User user, Product product) {
        final int QUANTITY = 1; // MVP에서는 수량을 1로 고정

        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PAYMENT_COMPLETED);
        order.setDeliveryStatus(DeliveryStatus.PREPARING);

        // 단가 * 수량(1) 으로 총액 계산
        order.setTotalPrice(product.getPrice());

        // 주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(product, product.getPrice(), QUANTITY);
        order.addOrderItem(orderItem);

        // 상품 재고 감소
        product.decreaseStock(QUANTITY);

        return order;
    }

    //== 연관관계 편의 메서드 ==//
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
        payment.setOrder(this);
    }
}
