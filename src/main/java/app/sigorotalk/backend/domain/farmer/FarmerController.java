package app.sigorotalk.backend.domain.farmer;

import app.sigorotalk.backend.common.response.ApiResponse;
import app.sigorotalk.backend.domain.farmer.dto.FarmerDetailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/farmers")
@RequiredArgsConstructor
public class FarmerController {

    private final FarmerService farmerService;

    @GetMapping("/{farmerId}")
    public ResponseEntity<ApiResponse<FarmerDetailResponseDto>> getFarmer(
            @PathVariable Long farmerId) {

        FarmerDetailResponseDto farmerDetails = farmerService.getFarmerDetails(farmerId);
        return ResponseEntity.ok(ApiResponse.success(farmerDetails));
    }
}
