package app.sigorotalk.backend.domain.diarie;

import app.sigorotalk.backend.common.response.ApiResponse;
import app.sigorotalk.backend.domain.diarie.dto.DiaryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/diaries")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DiaryResponseDto>>> getProductDiaries(
            @PathVariable Long productId) {

        List<DiaryResponseDto> diaries = diaryService.getDiariesForProduct(productId);
        return ResponseEntity.ok(ApiResponse.success(diaries));
    }
}