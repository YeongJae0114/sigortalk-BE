package app.sigorotalk.backend.domain.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByProductId(Long productId);

    /**
     * 특정 프로젝트 ID에 속한 모든 리뷰를 한 번의 쿼리로 조회 (N+1 문제 해결)
     * JOIN FETCH: 연관된 엔티티(user, product)를 즉시 로딩하여 추가 쿼리를 방지
     */
    @Query("SELECT r FROM Review r " +
            "JOIN FETCH r.user u " +
            "JOIN r.product p " +
            "WHERE p.farmProject.id = :projectId")
    List<Review> findAllByProjectIdWithDetails(@Param("projectId") Long projectId);

}
