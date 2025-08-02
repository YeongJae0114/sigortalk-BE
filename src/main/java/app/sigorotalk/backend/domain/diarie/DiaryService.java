package app.sigorotalk.backend.domain.diarie;

import app.sigorotalk.backend.domain.diarie.dto.DiaryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryService {

    private final DiaryRepository diaryRepository;

    public List<DiaryResponseDto> getDiariesForProduct(Long productId) {
        // 1. 레포지토리를 통해 특정 상품의 모든 재배 일지를 조회
        List<Diary> diaries = diaryRepository.findAllByProductIdOrderByCreatedAtDesc(productId);

        // 2. 조회된 엔티티 리스트를 DTO 리스트로 변환
        return diaries.stream()
                .map(DiaryResponseDto::from)
                .collect(Collectors.toList());
    }
}