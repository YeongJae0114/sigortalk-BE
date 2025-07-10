package app.sigorotalk.backend.domain.mentor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MentorRepository extends JpaRepository<Mentor, Long> {

    // 멘토 목록 조회 시 N+1 문제 해결을 위해 User를 fetch join
    @Query(value = "SELECT m FROM Mentor m JOIN FETCH m.user ORDER BY m.id",
            countQuery = "SELECT COUNT(m) FROM Mentor m")
    Page<Mentor> findAllWithUser(Pageable pageable);

    // 멘토 상세 조회 시 User를 fetch join
    @Query("SELECT m FROM Mentor m JOIN FETCH m.user WHERE m.id = :mentorId")
    Optional<Mentor> findByIdWithUser(@Param("mentorId") Long mentorId);

}
