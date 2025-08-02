package app.sigorotalk.backend.domain.diarie;

import app.sigorotalk.backend.common.entity.BaseTimeEntity;
import app.sigorotalk.backend.domain.product.Product;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;


@Entity
@Table(name = "diaries")
@Getter @Setter
@NoArgsConstructor
public class Diary extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String content;

    private String status;

    @Type(JsonType.class)
    @Column(name = "image_urls", columnDefinition = "json")
    private List<String> imageUrls;

    @Type(JsonType.class)
    @Column(name = "tags", columnDefinition = "json")
    private List<String> tags = new ArrayList<>();

}
