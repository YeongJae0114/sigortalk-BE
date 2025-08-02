package app.sigorotalk.backend.domain.farm_project;

import app.sigorotalk.backend.common.response.ApiResponse;
import app.sigorotalk.backend.domain.farm_project.dto.LatestProjectResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class FarmProjectController {

    private final FarmProjectService farmProjectService;

    @GetMapping("/latest")
    public ResponseEntity<ApiResponse<List<LatestProjectResponseDto>>> getLatestProjects() {
        List<LatestProjectResponseDto> latestProjects = farmProjectService.getLatest10Projects();
        return ResponseEntity.ok(ApiResponse.success(latestProjects));
    }
}