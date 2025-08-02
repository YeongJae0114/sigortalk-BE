package app.sigorotalk.backend.domain.product;
import app.sigorotalk.backend.common.entity.BaseTimeEntity;
import app.sigorotalk.backend.common.exception.BusinessException;
import app.sigorotalk.backend.domain.diarie.Diary;
import app.sigorotalk.backend.domain.farm_project.FarmProject;
import app.sigorotalk.backend.domain.growing.GrowingSchedule;
import app.sigorotalk.backend.domain.order_item.OrderItem;
import app.sigorotalk.backend.domain.review.Review;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import org.hibernate.annotations.Type;


@Entity
@Table(name = "products")
@Getter @Setter
@NoArgsConstructor
public class Product extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "farm_project_id")
    private FarmProject farmProject;

    private String name;

    private BigDecimal price;

    @Column(nullable = false)
    private int maxQuantity;

    private int stock;

    private String description;

    @Column(name = "funding_deadline")
    private LocalDate fundingDeadline;

    @Column(name = "nutrition_info", columnDefinition = "TEXT")
    private String nutritionInfo;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "delivery_fee")
    private BigDecimal deliveryFee;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GrowingSchedule> growingSchedules;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diary> diaries = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public void decreaseStock(int quantity) {
        int restStock = this.stock - quantity;
        if (restStock < 0) {
            throw new BusinessException(ProductErrorCode.NOT_ENOUGH_STOCK); // 재고 부족 예외 (별도 정의 필요)
        }
        this.stock = restStock;
    }
}
