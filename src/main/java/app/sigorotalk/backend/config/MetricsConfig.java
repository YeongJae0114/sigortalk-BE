package app.sigorotalk.backend.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Configuration
public class MetricsConfig {

    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> customMetricsFilter(MeterRegistry registry) {
        FilterRegistrationBean<OncePerRequestFilter> registration = new FilterRegistrationBean<>();

        registration.setFilter(new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
                    throws ServletException, IOException {
                long startTime = System.nanoTime();
                try {
                    chain.doFilter(request, response);
                } finally {
                    long duration = System.nanoTime() - startTime;

                    if (!response.isCommitted()) {
                        Timer.builder("http.server.requests")
                                .description("HTTP Server Requests")
                                .tag("method", request.getMethod())
                                .tag("uri", request.getRequestURI())
                                .tag("status", String.valueOf(response.getStatus()))
                                .register(registry)
                                .record(duration, TimeUnit.NANOSECONDS);
                    }
                }
            }
        });

        registration.setOrder(Ordered.HIGHEST_PRECEDENCE); // 가장 먼저 실행
        return registration;
    }
}


