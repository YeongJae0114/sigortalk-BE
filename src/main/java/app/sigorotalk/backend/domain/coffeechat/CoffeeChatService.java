package app.sigorotalk.backend.domain.coffeechat;

import app.sigorotalk.backend.common.exception.BusinessException;
import app.sigorotalk.backend.domain.coffeechat.dto.ChatStatusUpdateResponseDto;
import app.sigorotalk.backend.domain.coffeechat.dto.CoffeeChatApplyRequestDto;
import app.sigorotalk.backend.domain.coffeechat.dto.CoffeeChatApplyResponseDto;
import app.sigorotalk.backend.domain.coffeechat.dto.MyChatListResponseDto;
import app.sigorotalk.backend.domain.mentor.Mentor;
import app.sigorotalk.backend.domain.mentor.MentorRepository;
import app.sigorotalk.backend.domain.user.User;
import app.sigorotalk.backend.domain.user.UserErrorCode;
import app.sigorotalk.backend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoffeeChatService {
    private final CoffeeChatApplicationRepository coffeeChatApplicationRepository;
    private final UserRepository userRepository;
    private final MentorRepository mentorRepository;

    @Transactional
    public CoffeeChatApplyResponseDto applyForChat(CoffeeChatApplyRequestDto requestDto, Long menteeId) {
        User mentee = findUserById(menteeId);
        Mentor mentor = mentorRepository.findById(requestDto.getMentorId())
                .orElseThrow(() -> new BusinessException(CoffeeChatErrorCode.MENTOR_NOT_FOUND));

        CoffeeChatApplication application = CoffeeChatApplication.builder()
                .mentee(mentee)
                .mentor(mentor)
                .status(CoffeeChatApplication.Status.PENDING)
                .applicationDate(LocalDateTime.now())
                .build();

        CoffeeChatApplication savedApplication = coffeeChatApplicationRepository.save(application);
        return CoffeeChatApplyResponseDto.from(savedApplication);
    }

    @Transactional(readOnly = true)
    public List<MyChatListResponseDto> getMyChats(Long userId) {
        List<CoffeeChatApplication> myApplications = coffeeChatApplicationRepository.findMyChatsByUserId(userId);
        return myApplications.stream()
                .map(MyChatListResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChatStatusUpdateResponseDto acceptChat(Long applicationId, Long mentorUserId) {
        CoffeeChatApplication application = findApplicationById(applicationId);
        User mentorUser = findUserById(mentorUserId);

        application.accept(mentorUser);

        return ChatStatusUpdateResponseDto.from(application);
    }

    @Transactional
    public ChatStatusUpdateResponseDto rejectChat(Long applicationId, Long mentorUserId) {
        CoffeeChatApplication application = findApplicationById(applicationId);
        User mentorUser = findUserById(mentorUserId);

        application.reject(mentorUser);

        return ChatStatusUpdateResponseDto.from(application);
    }

    @Transactional
    public ChatStatusUpdateResponseDto completeChat(Long applicationId, Long menteeUserId) {
        CoffeeChatApplication application = findApplicationById(applicationId);
        User menteeUser = findUserById(menteeUserId);

        application.complete(menteeUser);

        return ChatStatusUpdateResponseDto.from(application);
    }

    // --- private 헬퍼 메서드: 중복되는 조회 로직 분리 ---
    private CoffeeChatApplication findApplicationById(Long applicationId) {
        return coffeeChatApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new BusinessException(CoffeeChatErrorCode.COFFEE_CHAT_NOT_FOUND));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));
    }
}


