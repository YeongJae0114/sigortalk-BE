package app.sigorotalk.backend.domain.product;

import app.sigorotalk.backend.common.response.ApiResponse;
import app.sigorotalk.backend.domain.product.dto.ProductResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class GetProductController {
    private final ProductServiceV2 productServiceV2;


    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getAllProducts() {
        List<ProductResponseDto> products = productServiceV2.getAllProducts();

        return ResponseEntity.ok(ApiResponse.success(products));
    }

}
