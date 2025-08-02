package app.sigorotalk.backend.domain.product.dto;

import app.sigorotalk.backend.domain.product.Product;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductResponseDto {

    private String title;
    private BigDecimal price;
    private String imageUrl;  // 단수형으로 변경

    public static ProductResponseDto from(Product product) {
        return ProductResponseDto.builder()
                .title(product.getName())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())  // 필드명에 맞게 변경
                .build();
    }

    public static List<ProductResponseDto> fromList(List<Product> products) {
        return products.stream()
                .map(ProductResponseDto::from)
                .collect(Collectors.toList());
    }
}
