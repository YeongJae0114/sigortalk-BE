package app.sigorotalk.backend.domain.diarie;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    /**
     * 특정 상품 ID에 해당하는 모든 재배 일지를 최신순으로 조회합니다.
     * @param productId 상품 ID
     * @return 재배 일지 리스트
     */
    List<Diary> findAllByProductIdOrderByCreatedAtDesc(Long productId);
}