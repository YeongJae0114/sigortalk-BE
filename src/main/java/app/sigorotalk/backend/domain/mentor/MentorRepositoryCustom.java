package app.sigorotalk.backend.domain.mentor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MentorRepositoryCustom {

    Page<Mentor> findByFilters(String region, String expertise, Pageable pageable);

}
