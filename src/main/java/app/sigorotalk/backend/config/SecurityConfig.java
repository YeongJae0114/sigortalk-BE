package app.sigorotalk.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화 (필요 시 활성화 가능)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/actuator/prometheus").permitAll() // 공개 URL
                        .anyRequest().authenticated() // 그 외 인증 필요
                )
                .httpBasic(AbstractHttpConfigurer::disable) // 🔥 기본 HTTP 인증 제거
                .formLogin(AbstractHttpConfigurer::disable); // 🔥 로그인 폼 제거

        return http.build();
    }
}
