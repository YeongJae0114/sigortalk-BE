package app.sigorotalk.backend.domain.farm_project;

import app.sigorotalk.backend.domain.farm_project.dto.LatestProjectResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FarmProjectService {

    private final FarmProjectRepository farmProjectRepository;
    // ReviewRepository는 더 이상 필요 없으므로 삭제

    public List<LatestProjectResponseDto> getLatest10Projects() {
        // 1. 최신 프로젝트 9개 조회
        List<FarmProject> latestProjects = farmProjectRepository.findTop9ByOrderByCreatedAtAsc();

        // 2. 각 프로젝트를 DTO로 바로 변환 (리뷰 로직 완전 삭제)
        return latestProjects.stream()
                .map(LatestProjectResponseDto::from)
                .collect(Collectors.toList());
    }
}