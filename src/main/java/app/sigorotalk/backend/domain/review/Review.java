package app.sigorotalk.backend.domain.review;

import app.sigorotalk.backend.common.entity.BaseTimeEntity;
import app.sigorotalk.backend.domain.order.Order;
import app.sigorotalk.backend.domain.project.Project;
import app.sigorotalk.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_review")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    // 기존 CoffeeChatApplication 관계를 Order로 변경 (하나의 주문에 하나의 리뷰)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    // 리뷰가 어떤 프로젝트에 대한 것인지 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    // 리뷰를 작성한 사용자 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer rating;

    @Lob
    private String comment;
}