package app.sigorotalk.backend.config.coffeechat;

import app.sigorotalk.backend.domain.coffeechat.CoffeeChatApplication;
import app.sigorotalk.backend.domain.coffeechat.CoffeeChatApplicationRepository;
import app.sigorotalk.backend.domain.coffeechat.CoffeeChatService;
import app.sigorotalk.backend.domain.coffeechat.dto.CoffeeChatApplyRequestDto;
import app.sigorotalk.backend.domain.coffeechat.dto.CoffeeChatApplyResponseDto;
import app.sigorotalk.backend.domain.coffeechat.dto.MyChatListResponseDto;
import app.sigorotalk.backend.domain.mentor.Mentor;
import app.sigorotalk.backend.domain.mentor.MentorRepository;
import app.sigorotalk.backend.domain.user.User;
import app.sigorotalk.backend.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoffeeChatServiceTest {

    @InjectMocks
    private CoffeeChatService coffeeChatService;
    @Mock
    private CoffeeChatApplicationRepository coffeeChatApplicationRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MentorRepository mentorRepository;

    @Test
    @DisplayName("신청 성공: applyForChat은 PENDING 상태의 신청서를 생성하고 save를 호출한다.")
    void applyForChat_Success() {
        long menteeId = 1L;
        long mentorId = 2L;
        CoffeeChatApplyRequestDto requestDto = new CoffeeChatApplyRequestDto();
        ReflectionTestUtils.setField(requestDto, "mentorId", mentorId);
        User mentee = User.builder().id(menteeId).build();
        User mentorUser = User.builder().id(mentorId).name("테스트멘토").build();
        Mentor mentor = Mentor.builder().id(mentorId).user(mentorUser).build();

        when(userRepository.findById(menteeId)).thenReturn(Optional.of(mentee));
        when(mentorRepository.findById(mentorId)).thenReturn(Optional.of(mentor));
        when(coffeeChatApplicationRepository.save(any(CoffeeChatApplication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CoffeeChatApplyResponseDto response = coffeeChatService.applyForChat(requestDto, menteeId);

        verify(coffeeChatApplicationRepository, times(1)).save(any(CoffeeChatApplication.class));
        assertThat(response.status()).isEqualTo(CoffeeChatApplication.Status.PENDING.name());
        assertThat(response.mentorName()).isEqualTo("테스트멘토");
    }

    @Test
    @DisplayName("수락 성공: acceptChat은 올바른 엔티티를 찾아 accept() 메서드를 호출한다.")
    void acceptChat_Success() {
        long applicationId = 1L;
        long mentorUserId = 2L;
        User mentorUser = User.builder().id(mentorUserId).build();
        CoffeeChatApplication application = spy(CoffeeChatApplication.builder().status(CoffeeChatApplication.Status.PENDING).build());

        when(coffeeChatApplicationRepository.findById(applicationId)).thenReturn(Optional.of(application));
        when(userRepository.findById(mentorUserId)).thenReturn(Optional.of(mentorUser));
        doNothing().when(application).accept(any(User.class));

        coffeeChatService.acceptChat(applicationId, mentorUserId);

        verify(application, times(1)).accept(mentorUser);
    }

    @Test
    @DisplayName("거절 성공: rejectChat은 올바른 엔티티를 찾아 reject() 메서드를 호출한다.")
    void rejectChat_Success() {
        long applicationId = 1L;
        long mentorUserId = 2L;
        User mentorUser = User.builder().id(mentorUserId).build();
        CoffeeChatApplication application = spy(CoffeeChatApplication.builder().status(CoffeeChatApplication.Status.PENDING).build());

        when(coffeeChatApplicationRepository.findById(applicationId)).thenReturn(Optional.of(application));
        when(userRepository.findById(mentorUserId)).thenReturn(Optional.of(mentorUser));
        doNothing().when(application).reject(any(User.class));

        coffeeChatService.rejectChat(applicationId, mentorUserId);

        verify(application, times(1)).reject(mentorUser);
    }

    @Test
    @DisplayName("완료 성공: completeChat은 올바른 엔티티를 찾아 complete() 메서드를 호출한다.")
    void completeChat_Success() {
        long applicationId = 1L;
        long menteeUserId = 3L;
        User menteeUser = User.builder().id(menteeUserId).build();
        CoffeeChatApplication application = spy(CoffeeChatApplication.builder().status(CoffeeChatApplication.Status.ACCEPTED).build());

        when(coffeeChatApplicationRepository.findById(applicationId)).thenReturn(Optional.of(application));
        when(userRepository.findById(menteeUserId)).thenReturn(Optional.of(menteeUser));
        doNothing().when(application).complete(any(User.class));

        coffeeChatService.completeChat(applicationId, menteeUserId);

        verify(application, times(1)).complete(menteeUser);
    }

    @Test
    @DisplayName("목록 조회 성공: getMyChats는 repository의 조회 결과를 DTO 리스트로 변환한다.")
    void getMyChats_Success() {
        // given
        long userId = 1L;
        User user = User.builder().id(userId).name("테스트유저").build();
        Mentor mentor = Mentor.builder().user(user).build();
        CoffeeChatApplication app1 = CoffeeChatApplication.builder().mentor(mentor).mentee(user).status(CoffeeChatApplication.Status.PENDING).build();
        CoffeeChatApplication app2 = CoffeeChatApplication.builder().mentor(mentor).mentee(user).status(CoffeeChatApplication.Status.ACCEPTED).build();

        when(coffeeChatApplicationRepository.findMyChatsByUserId(userId)).thenReturn(List.of(app1, app2));

        // when
        List<MyChatListResponseDto> result = coffeeChatService.getMyChats(userId);

        // then
        assertThat(result).hasSize(2);
        verify(coffeeChatApplicationRepository, times(1)).findMyChatsByUserId(userId);
    }
}