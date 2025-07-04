package app.sigorotalk.backend.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 사용자를 찾는 쿼리 메서드
    Optional<User> findByEmail(String email);
}
