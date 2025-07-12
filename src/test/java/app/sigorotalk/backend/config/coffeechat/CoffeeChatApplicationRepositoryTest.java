package app.sigorotalk.backend.config.coffeechat;

import app.sigorotalk.backend.domain.coffeechat.CoffeeChatApplication;
import app.sigorotalk.backend.domain.coffeechat.CoffeeChatApplicationRepository;
import app.sigorotalk.backend.domain.mentor.Mentor;
import app.sigorotalk.backend.domain.mentor.MentorRepository;
import app.sigorotalk.backend.domain.user.User;
import app.sigorotalk.backend.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CoffeeChatApplicationRepositoryTest {

    @Autowired
    private CoffeeChatApplicationRepository coffeeChatApplicationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MentorRepository mentorRepository;

    private User user1, user2, user3;
    private Mentor mentor2, mentor3;

    @BeforeEach
    void setUp() {
        user1 = userRepository.save(User.builder().email("user1@test.com").name("유저1").password("pw").role(User.Role.ROLE_USER).build());
        user2 = userRepository.save(User.builder().email("user2@test.com").name("유저2_멘토").password("pw").role(User.Role.ROLE_MENTOR).build());
        user3 = userRepository.save(User.builder().email("user3@test.com").name("유저3_멘토").password("pw").role(User.Role.ROLE_MENTOR).build());

        mentor2 = mentorRepository.save(Mentor.builder().user(user2).build());
        mentor3 = mentorRepository.save(Mentor.builder().user(user3).build());
    }

    @Test
    @DisplayName("findMyChatsByUserId: 특정 유저가 멘토이거나 멘티인 모든 커피챗을 조회한다.")
    void findMyChatsByUserId_whenUserIsBothMentorAndMentee() {
        // given
        // 시나리오 1: user2가 멘토, user1이 멘티
        CoffeeChatApplication app1 = coffeeChatApplicationRepository.save(CoffeeChatApplication.builder()
                .mentor(mentor2).mentee(user1).status(CoffeeChatApplication.Status.PENDING).applicationDate(LocalDateTime.now().minusDays(2)).build());
        // 시나리오 2: user3이 멘토, user2가 멘티
        CoffeeChatApplication app2 = coffeeChatApplicationRepository.save(CoffeeChatApplication.builder()
                .mentor(mentor3).mentee(user2).status(CoffeeChatApplication.Status.ACCEPTED).applicationDate(LocalDateTime.now().minusDays(1)).build());
        // 시나리오 3: user2와 무관한 채팅
        coffeeChatApplicationRepository.save(CoffeeChatApplication.builder()
                .mentor(mentor3).mentee(user1).status(CoffeeChatApplication.Status.COMPLETED).applicationDate(LocalDateTime.now()).build());

        // when (user2로 조회)
        List<CoffeeChatApplication> result = coffeeChatApplicationRepository.findMyChatsByUserId(user2.getId());

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(CoffeeChatApplication::getId)
                .containsExactlyInAnyOrder(app1.getId(), app2.getId());

        // JOIN FETCH 검증
        assertThat(result.get(0).getMentor().getUser()).isNotNull();
        assertThat(result.get(0).getMentee()).isNotNull();
    }
}