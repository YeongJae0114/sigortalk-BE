package app.sigorotalk.backend.domain.farmer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FarmerRepository extends JpaRepository<Farmer, Long> {

    /**
     * 특정 농장주의 모든 정보를 한 번의 쿼리로 조회 (N+1 문제 해결)
     * user, farmProjects까지 JOIN FETCH로 즉시 로딩합니다.
     *
     * @param farmerId 조회할 농장주의 ID
     * @return Farmer 엔티티 Optional
     */
    @Query("SELECT f FROM Farmer f " +
            "LEFT JOIN FETCH f.user " +
            "LEFT JOIN FETCH f.farmProjects " +
            "WHERE f.id = :farmerId")
    Optional<Farmer> findByIdWithDetails(@Param("farmerId") Long farmerId);

}