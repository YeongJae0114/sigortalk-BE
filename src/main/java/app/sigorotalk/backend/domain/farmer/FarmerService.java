package app.sigorotalk.backend.domain.farmer;

import app.sigorotalk.backend.common.exception.BusinessException;
import app.sigorotalk.backend.common.exception.CommonErrorCode;
import app.sigorotalk.backend.domain.farmer.dto.FarmerDetailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FarmerService {

    private final FarmerRepository farmerRepository;

    public FarmerDetailResponseDto getFarmerDetails(Long farmerId) {
        // 1. Repository에 추가한 효율적인 쿼리 메서드를 호출하여 Farmer 정보를 조회합니다.
        Farmer farmer = farmerRepository.findByIdWithDetails(farmerId)
                .orElseThrow(() -> new BusinessException(CommonErrorCode.NOT_FOUND)); // 농장주가 없으면 404 예외

        // 2. 조회된 Farmer 엔티티를 DTO로 변환하여 반환합니다.
        return FarmerDetailResponseDto.from(farmer);
    }
}