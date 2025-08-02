package app.sigorotalk.backend.config;

import app.sigorotalk.backend.config.jwt.JwtAuthenticationEntryPoint;
import app.sigorotalk.backend.config.jwt.JwtAuthenticationFilter;
import app.sigorotalk.backend.config.jwt.JwtTokenProvider;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    public static final String[] SWAGGER_URLS = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/h2-console/**"
    };

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 기본 설정
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                // 세션 STATELESS 설정
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 예외 처리 설정 추가
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))

                // 요청 경로별 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/prometheus").permitAll() // 모니터링 로그 수집 엔드포인트 허용!
                        .requestMatchers(SWAGGER_URLS).permitAll()
                        .requestMatchers("/api/auth/**", "/api/users", "/login", "/api/**").permitAll() // 로그인, 회원가입은 허용
                        .anyRequest().authenticated() // 나머지는 인증 필요
                )

                // JWT 필터 추가
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // CORS 설정 정의
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // 허용할 origin 목록 (배포 주소, 로컬 테스트 주소 등)
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "https://coffeebara-front.web.app/",
                "https://coffeebara-front.web.app"
        ));
        // 허용할 HTTP 메서드
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 허용할 헤더
        config.setAllowedHeaders(Arrays.asList("*"));
        // 인증 정보(쿠키, Authorization 헤더) 허용 여부
        config.setAllowCredentials(true);
        // pre-flight request 캐싱 기간 (초)
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 모든 경로에 대해 위 CORS 설정 적용
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
