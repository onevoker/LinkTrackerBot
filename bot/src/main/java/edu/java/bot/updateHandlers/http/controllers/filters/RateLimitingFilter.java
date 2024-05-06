package edu.java.bot.updateHandlers.http.controllers.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.dto.response.ApiErrorResponse;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class RateLimitingFilter extends OncePerRequestFilter {
    private final Bucket bucket;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final int TOKEN_COUNT = 1;
    private static final int TOO_MANY_REQUESTS_STATUS_CODE = 429;
    private static final String ENCODING = "UTF-8";
    private static final String EXCEPTION_MSG =
        "Неправильно настроены ретраи для http, слишком много запросов, я не могу их обработать";

    @Override
    protected void doFilterInternal(
        @NotNull HttpServletRequest request,
        @NotNull HttpServletResponse response,
        @NotNull FilterChain filterChain
    ) throws IOException, ServletException {
        if (bucket.tryConsume(TOKEN_COUNT)) {
            filterChain.doFilter(request, response);
        } else {
            sendErrorResponse(response);
        }
    }

    private void sendErrorResponse(HttpServletResponse response) throws IOException {
        response.setStatus(TOO_MANY_REQUESTS_STATUS_CODE);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(ENCODING);

        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
            .exceptionMessage(EXCEPTION_MSG)
            .code(String.valueOf(TOO_MANY_REQUESTS_STATUS_CODE))
            .build();
        String json = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().write(json);
    }
}
