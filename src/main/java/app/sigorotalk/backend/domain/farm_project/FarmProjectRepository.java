package app.sigorotalk.backend.domain.farm_project;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FarmProjectRepository extends JpaRepository<FarmProject, Long> {
    List<FarmProject> findTop10ByOrderByCreatedAtDesc();
}
