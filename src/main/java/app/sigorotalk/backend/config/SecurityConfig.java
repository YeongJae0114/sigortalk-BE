package app.sigorotalk.backend.config;

import app.sigorotalk.backend.config.handler.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화 (필요 시 활성화 가능)
                .requestCache(AbstractHttpConfigurer::disable) // ✅ 요청 캐시 비활성화 (불필요한 /error 재요청 방지
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint) // 🔥 커스텀 EntryPoint 등록
                )
                .cors(cors -> {})
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/actuator/prometheus").permitAll() // 공개 URL
                        .anyRequest().authenticated() // 그 외 인증 필요
                )
                .httpBasic(AbstractHttpConfigurer::disable) // 🔥 기본 HTTP 인증 제거
                .formLogin(AbstractHttpConfigurer::disable); // 🔥 로그인 폼 제거

        return http.build();
    }
}
