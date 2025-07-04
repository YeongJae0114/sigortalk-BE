package app.sigorotalk.backend.domain.coffeechat;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chats")
public class CoffeeChatController {

    // TODO: [POST /] 커피챗 신청 API (Body에 mentorId 포함)
    // public ResponseEntity<?> applyForChat( ... )

    // TODO: [GET /me] 나의 커피챗 목록 조회 API (멘토/멘티 공용)
    // public ResponseEntity<?> getMyChats( ... )

    // TODO: [PATCH /{chatId}/accept] (멘토) 커피챗 수락 API
    // public ResponseEntity<?> acceptChat( ... )

    // TODO: [PATCH /{chatId}/reject] (멘토) 커피챗 거절 API
    // public ResponseEntity<?> rejectChat( ... )

    // TODO: [PATCH /{chatId}/complete] (멘티) 커피챗 완료 API
    // public ResponseEntity<?> completeChat( ... )

}
