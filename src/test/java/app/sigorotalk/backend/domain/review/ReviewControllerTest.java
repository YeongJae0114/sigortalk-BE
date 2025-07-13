package app.sigorotalk.backend.domain.review;


import app.sigorotalk.backend.config.jwt.JwtTokenProvider;
import app.sigorotalk.backend.domain.coffeechat.CoffeeChatApplication;
import app.sigorotalk.backend.domain.coffeechat.CoffeeChatApplicationRepository;
import app.sigorotalk.backend.domain.mentor.Mentor;
import app.sigorotalk.backend.domain.mentor.MentorRepository;
import app.sigorotalk.backend.domain.review.dto.ReviewRequestDto;
import app.sigorotalk.backend.domain.user.User;
import app.sigorotalk.backend.domain.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
// @Transactional 제거!
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MentorRepository mentorRepository;
    @Autowired
    private CoffeeChatApplicationRepository coffeeChatApplicationRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private User mentee;
    private Mentor mentor;
    private CoffeeChatApplication completedChat;

    @BeforeEach
    void setUp() {
        mentee = userRepository.save(User.builder().email("mentee@test.com").name("테스트멘티").password("pw").role(User.Role.ROLE_USER).build());
        User mentorUser = userRepository.save(User.builder().email("mentor@test.com").name("테스트멘토").password("pw").role(User.Role.ROLE_MENTOR).build());
        mentor = mentorRepository.save(Mentor.builder().user(mentorUser).reviewCount(0).averageRating(BigDecimal.ZERO).build());
        completedChat = coffeeChatApplicationRepository.save(CoffeeChatApplication.builder().mentor(mentor).mentee(mentee).status(CoffeeChatApplication.Status.COMPLETED).build());
    }

    @AfterEach
    void tearDown() {
        reviewRepository.deleteAllInBatch();
        coffeeChatApplicationRepository.deleteAllInBatch();
        mentorRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("성공: 멘티가 완료된 커피챗에 리뷰를 작성하면, 비동기 처리가 완료된 후 멘토 평점이 정상적으로 업데이트된다.")
    void createReview_Async_E2E_Success() throws Exception {
        // given
        Authentication authentication = new UsernamePasswordAuthenticationToken(mentee.getId(), null, Collections.singletonList(new SimpleGrantedAuthority(mentee.getRole().toString())));
        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        ReviewRequestDto requestDto = createReviewRequestDto(completedChat.getId(), 5, "정말 유익한 시간이었습니다!");
        String requestBody = objectMapper.writeValueAsString(requestDto);

        // when
        mockMvc.perform(post("/api/v1/reviews")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andDo(print());

        // then
        await().atMost(5, TimeUnit.SECONDS).pollInterval(100, TimeUnit.MILLISECONDS).untilAsserted(() -> {
            Mentor updatedMentor = mentorRepository.findById(mentor.getId())
                    .orElseThrow(() -> new AssertionError("멘토를 찾을 수 없습니다.")); // 에러 발생 시 더 명확한 메시지
            assertThat(updatedMentor.getReviewCount()).isEqualTo(1);
            assertThat(updatedMentor.getAverageRating()).isEqualByComparingTo(new BigDecimal("5.0"));
        });
    }

    private ReviewRequestDto createReviewRequestDto(Long chatId, int rating, String comment) {
        ReviewRequestDto dto = new ReviewRequestDto();
        ReflectionTestUtils.setField(dto, "chatId", chatId);
        ReflectionTestUtils.setField(dto, "rating", rating);
        ReflectionTestUtils.setField(dto, "comment", comment);
        return dto;
    }
}