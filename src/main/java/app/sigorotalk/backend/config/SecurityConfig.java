package app.sigorotalk.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (필요 시 활성화 가능)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/actuator/prometheus").permitAll() // 공개 URL
                        .anyRequest().authenticated() // 그 외 인증 필요
                )
                .formLogin(Customizer.withDefaults()) // 기본 로그인 폼 사용
                .logout(Customizer.withDefaults());   // 기본 로그아웃 설정

        return http.build();
    }
}
