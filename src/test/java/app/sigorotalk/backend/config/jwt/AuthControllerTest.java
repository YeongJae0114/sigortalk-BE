package app.sigorotalk.backend.config.jwt;

import app.sigorotalk.backend.domain.auth.dto.LoginRequestDto;
import app.sigorotalk.backend.domain.user.User;
import app.sigorotalk.backend.domain.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // 각 테스트 후 DB 롤백
class AuthControllerTest {

    // 테스트용 사용자 정보
    private final String testEmail = "testuser@example.com";
    private final String testPassword = "password123";
    @Autowired
    private MockMvc mockMvc; // HTTP 요청을 흉내내는 객체
    @Autowired
    private ObjectMapper objectMapper; // 객체를 JSON 문자열로 변환
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        // 모든 테스트 전에 DB에 테스트용 사용자를 미리 저장
        User testUser = User.builder()
                .email(testEmail)
                .password(passwordEncoder.encode(testPassword)) // 비밀번호는 암호화해서 저장
                .name("Test User")
                .role(User.Role.ROLE_USER)
                .build();
        userRepository.save(testUser);
    }

    @Test
    @DisplayName("로그인 성공: 올바른 정보로 로그인 시, JWT를 포함하여 200 OK를 반환한다.")
    void login_Success() throws Exception {
        // given: 테스트 준비
        LoginRequestDto loginRequestDto = new LoginRequestDto(testEmail, testPassword);
        String requestBody = objectMapper.writeValueAsString(loginRequestDto);

        // when & then: 테스트 실행 및 결과 검증
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk()) // 상태 코드가 200 OK 인지 확인
                .andExpect(header().exists("Authorization")) // Authorization 헤더가 존재하는지 확인
                .andExpect(jsonPath("$.success").value(true)) // 응답 본문의 success 필드가 true인지 확인
                .andExpect(jsonPath("$.response.accessToken").exists()) // accessToken이 존재하는지 확인
                .andDo(print()); // 요청/응답 전체 내용 출력
    }

    @Test
    @DisplayName("로그인 실패: 잘못된 비밀번호로 로그인 시, 401 Unauthorized를 반환한다.")
    void login_Failure_WrongPassword() throws Exception {
        // given
        LoginRequestDto loginRequestDto = new LoginRequestDto(testEmail, "wrongpassword");
        String requestBody = objectMapper.writeValueAsString(loginRequestDto);

        // when & then
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized()) // 상태 코드가 401 Unauthorized 인지 확인
                .andDo(print());
    }

    @Test
    @DisplayName("보호된 API 접근 성공: 유효한 JWT로 접근 시, 200 OK를 반환한다.")
    void protectedEndpoint_Success_WithValidToken() throws Exception {
        // given: 먼저 로그인을 통해 유효한 토큰을 얻어옴
        LoginRequestDto loginRequestDto = new LoginRequestDto(testEmail, testPassword);
        String requestBody = objectMapper.writeValueAsString(loginRequestDto);

        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        String authHeader = loginResult.getResponse().getHeader("Authorization");

        // when & then: 얻어온 토큰으로 보호된 API에 접근
        // (미리 /api/v1/test/me 같은 간단한 테스트용 API가 있다고 가정)
        mockMvc.perform(get("/api/v1/users/me") // '/users/me' 같은 보호된 엔드포인트
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("보호된 API 접근 실패: JWT 없이 접근 시, 401 Unauthorized를 반환한다.")
    void protectedEndpoint_Failure_NoToken() throws Exception {
        // when & then
        mockMvc.perform(get("/api/v1/users/me")) // 토큰 없이 요청
                .andExpect(status().isUnauthorized()) // 401 에러를 기대
                .andDo(print());
    }
}
