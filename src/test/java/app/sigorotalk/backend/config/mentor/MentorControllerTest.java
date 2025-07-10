package app.sigorotalk.backend.config.mentor;

import app.sigorotalk.backend.domain.mentor.Mentor;
import app.sigorotalk.backend.domain.mentor.MentorRepository;
import app.sigorotalk.backend.domain.user.User;
import app.sigorotalk.backend.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MentorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MentorRepository mentorRepository;

    private Mentor savedMentor;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 생성
        User testUser = User.builder()
                .email("mentor@example.com")
                .password("password")
                .name("테스트멘토")
                .role(User.Role.ROLE_MENTOR)
                .build();
        userRepository.save(testUser);

        savedMentor = Mentor.builder()
                .user(testUser)
                .shortDescription("테스트 짧은 설명")
                .expertise("Java, Spring")
                .build();
        mentorRepository.save(savedMentor);
    }

    @Test
    @DisplayName("멘토 목록 조회 API 성공: 200 OK와 함께 페이징된 데이터를 반환한다.")
    @WithMockUser
    // 인증된 사용자로 간주
    void getMentorListApi_Success() throws Exception {
        // when & then
        mockMvc.perform(get("/api/v1/mentors")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response.content[0].name").value("테스트멘토"))
                .andExpect(jsonPath("$.response.content[0].expertise").value("Java, Spring"))
                .andDo(print());
    }

    @Test
    @DisplayName("멘토 상세 조회 API 성공: 200 OK와 함께 상세 데이터를 반환한다.")
    @WithMockUser
    void getMentorDetailApi_Success() throws Exception {
        // when & then
        mockMvc.perform(get("/api/v1/mentors/" + savedMentor.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response.mentorId").value(savedMentor.getId()))
                .andExpect(jsonPath("$.response.name").value("테스트멘토"))
                .andDo(print());
    }

    @Test
    @DisplayName("멘토 상세 조회 API 실패: 존재하지 않는 ID 요청 시 404 Not Found를 반환한다.")
    @WithMockUser
    void getMentorDetailApi_Failure_NotFound() throws Exception {
        // when & then
        mockMvc.perform(get("/api/v1/mentors/9999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.message").value("요청한 리소스를 찾을 수 없습니다."))
                .andDo(print());
    }
}