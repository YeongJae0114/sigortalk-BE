package app.sigorotalk.backend.domain.mentor;

import app.sigorotalk.backend.common.response.ApiResponse;
import app.sigorotalk.backend.domain.mentor.dto.MentorDetailResponseDto;
import app.sigorotalk.backend.domain.mentor.dto.MentorListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mentors")
@RequiredArgsConstructor
public class MentorController {

    private final MentorService mentorService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<MentorListResponseDto>>> getMentorList(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String expertise,
            Pageable pageable
    ) {
        Page<MentorListResponseDto> mentorList = mentorService.getMentorList(region, expertise, pageable); // 수정
        return ResponseEntity.ok(ApiResponse.success(mentorList));
    }

    @GetMapping("/{mentorId}")
    public ResponseEntity<ApiResponse<MentorDetailResponseDto>> getMentorDetail(@PathVariable("mentorId") Long mentorId) {
        MentorDetailResponseDto mentorDetail = mentorService.getMentorDetail(mentorId);
        return ResponseEntity.ok(ApiResponse.success(mentorDetail));
    }
}
