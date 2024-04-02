package edu.java.bot.controllers.filters;

import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class RateLimitingFilter extends OncePerRequestFilter {
    private final Bucket bucket;
    private static final int TOKEN_COUNT = 1;
    private static final int TOO_MANY_REQUESTS_STATUS_CODE = 429;

    @Override
    protected void doFilterInternal(
        @NotNull HttpServletRequest request,
        @NotNull HttpServletResponse response,
        @NotNull FilterChain filterChain
    ) throws IOException, ServletException {
        if (bucket.tryConsume(TOKEN_COUNT)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(TOO_MANY_REQUESTS_STATUS_CODE);
        }
    }
}
