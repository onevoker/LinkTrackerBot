package edu.java.scrapper.controllers.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.response.ApiErrorResponse;
import edu.java.scrapper.configuration.ApplicationConfig;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class RateLimitingFilter extends OncePerRequestFilter {
    private final BucketFactory bucketFactory;
    private final ApplicationConfig.SwaggerEndpoints swaggerEndpoints;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<Long, Bucket> buckets = new ConcurrentHashMap<>();
    private static final int TOKEN_COUNT = 1;
    private static final String ID_HEADER = "Tg-Chat-Id";
    private static final int TOO_MANY_REQUESTS_STATUS_CODE = 429;
    private static final String DDOS_MESSAGE = "Слишком много запросов, хватит дудосить!";
    private static final String ENCODING = "UTF-8";

    @Override
    protected void doFilterInternal(
        @NotNull HttpServletRequest request,
        @NotNull HttpServletResponse response,
        @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (uri.contains(swaggerEndpoints.swagger()) || uri.contains(swaggerEndpoints.apiDocs())) {
            filterChain.doFilter(request, response);
        } else {
            filterTelegramRequest(request, response, filterChain);
        }
    }

    private void filterTelegramRequest(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        Long tgId = Long.parseLong(request.getHeader(ID_HEADER));
        Bucket bucket = buckets.computeIfAbsent(tgId, k -> bucketFactory.newBucket());
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
            .exceptionMessage(DDOS_MESSAGE)
            .code(String.valueOf(TOO_MANY_REQUESTS_STATUS_CODE))
            .build();
        String json = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().write(json);
    }
}
