//package app.sigorotalk.backend.domain.auth;
//
//
//import app.sigorotalk.backend.domain.auth.dto.LoginRequestDto;
//import app.sigorotalk.backend.domain.user.User;
//import app.sigorotalk.backend.domain.user.UserRepository;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.http.Cookie;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.util.ReflectionTestUtils;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Transactional
//class AuthControllerTest {
//
//    private final String testEmail = "testuser@example.com";
//    private final String testPassword = "password123";
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper objectMapper;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @BeforeEach
//    void setUp() {
//        User testUser = User.builder()
//                .email(testEmail)
//                .password(passwordEncoder.encode(testPassword))
//                .name("Test User")
//                .userType(User.UserType.BUYER)
//                .build();
//        userRepository.save(testUser);
//    }
//
//    @Test
//    @DisplayName("로그인 성공: 올바른 정보로 로그인 시, JWT와 사용자 정보를 포함하여 200 OK를 반환한다.")
//    void login_Success() throws Exception {
//        // given
//        LoginRequestDto loginRequestDto = createLoginRequestDto(testEmail, testPassword); // 헬퍼 메소드 사용
//        String requestBody = objectMapper.writeValueAsString(loginRequestDto);
//
//        // when & then
//        mockMvc.perform(post("/api/v1/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.response.accessToken").exists())
//                .andExpect(jsonPath("$.response.user.name").value("Test User"))
//                .andExpect(jsonPath("$.response.user.email").value(testEmail))
//                .andExpect(jsonPath("$.response.user.role").value("BUYER"))
//                .andExpect(cookie().exists("refresh_token"))
//                .andExpect(cookie().httpOnly("refresh_token", true))
//                .andExpect(cookie().path("refresh_token", "/"))
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("로그인 실패: 잘못된 비밀번호로 로그인 시, 401 Unauthorized를 반환한다.")
//    void login_Failure_WrongPassword() throws Exception {
//        // given
//        LoginRequestDto loginRequestDto = createLoginRequestDto(testEmail, "wrongpassword"); // 헬퍼 메소드 사용
//        String requestBody = objectMapper.writeValueAsString(loginRequestDto);
//
//        // when & then
//        mockMvc.perform(post("/api/v1/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(status().isUnauthorized())
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("보호된 API 접근 성공: 유효한 JWT로 접근 시, 200 OK를 반환한다.")
//    void protectedEndpoint_Success_WithValidToken() throws Exception {
//        // given: 먼저 로그인을 통해 유효한 토큰을 얻어옴
//        LoginRequestDto loginRequestDto = createLoginRequestDto(testEmail, testPassword); // 헬퍼 메소드 사용
//        String requestBody = objectMapper.writeValueAsString(loginRequestDto);
//
//        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // 응답 본문(JSON)에서 accessToken 추출
//        String responseBody = loginResult.getResponse().getContentAsString();
//        JsonNode responseJson = objectMapper.readTree(responseBody);
//        String accessToken = responseJson.at("/response/accessToken").asText();
//
//        // when & then: 얻어온 토큰으로 보호된 API에 접근
//        mockMvc.perform(get("/api/v1/users/me")
//                        .header("Authorization", "Bearer " + accessToken)) // Bearer 접두사 추가
//                .andExpect(status().isOk())
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("보호된 API 접근 실패: JWT 없이 접근 시, 401 Unauthorized를 반환한다.")
//    void protectedEndpoint_Failure_NoToken() throws Exception {
//        // when & then
//        mockMvc.perform(get("/api/v1/users/me"))
//                .andExpect(status().isUnauthorized())
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("API 재발급 성공: 유효한 Refresh Token 쿠키로 /refresh 요청 시 새 Access Token과 200 OK를 반환한다.")
//    void refreshApi_Success() throws Exception {
//        // given: 먼저 로그인을 통해 유효한 토큰들을 얻어옴
//        LoginRequestDto loginDto = createLoginRequestDto(testEmail, testPassword);
//        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginDto)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // 1. 응답에서 JSON 대신 'refresh_token' 쿠키를 추출합니다.
//        Cookie refreshTokenCookie = loginResult.getResponse().getCookie("refresh_token");
//
//        // 쿠키가 정상적으로 생성되었는지 확인
//        assertNotNull(refreshTokenCookie, "Refresh token cookie should not be null");
//
//        // 2. RefreshRequestDto는 더 이상 필요 없으므로 관련 코드를 삭제합니다.
//
//        // when & then: 얻어온 Refresh Token 쿠키로 재발급 API에 접근
//        mockMvc.perform(post("/api/v1/auth/refresh")
//                                // 3. .cookie()를 사용해 요청에 쿠키를 담아 보냅니다.
//                                .cookie(refreshTokenCookie)
//                        // 4. contentType과 content는 요청 body가 없으므로 삭제합니다.
//                )
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.response.accessToken").exists())
//                .andDo(print());
//    }
//
//    // LoginRequestDto 생성을 위한 private 헬퍼 메소드
//    private LoginRequestDto createLoginRequestDto(String email, String password) {
//        LoginRequestDto dto = new LoginRequestDto();
//        ReflectionTestUtils.setField(dto, "email", email);
//        ReflectionTestUtils.setField(dto, "password", password);
//        return dto;
//    }
//}