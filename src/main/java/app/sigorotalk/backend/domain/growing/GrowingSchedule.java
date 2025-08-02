package app.sigorotalk.backend.domain.growing;

import app.sigorotalk.backend.domain.product.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "growing_schedules")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrowingSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 일정 고유 ID

    private LocalDate date; // 재배 날짜

    private String title;   // 예: 파종, 수확 등

    @Column(columnDefinition = "TEXT")
    private String content; // 상세 설명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}

