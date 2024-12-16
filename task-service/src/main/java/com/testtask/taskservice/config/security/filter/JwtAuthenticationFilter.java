package com.testtask.taskservice.config.security.filter;

import com.testtask.taskservice.config.security.jwt.JwtTokenValidator;
import com.testtask.taskservice.model.dto.response.ApiResponse;
import com.testtask.taskservice.model.security.CustomUserPrincipal;
import com.testtask.taskservice.service.user.UserClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenValidator jwtTokenValidator;

    private final UserClient userClient;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws IOException {
        try {
            if (shouldSkipFilter(request.getRequestURI())) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = extractToken(request.getHeader(HttpHeaders.AUTHORIZATION));
            if (token == null) {
                sendErrorResponse(response, "Missing or invalid Authorization header");
                return;
            }

            if (!jwtTokenValidator.validateToken(token)) {
                sendErrorResponse(response, "Invalid token");
                return;
            }

            if (!authenticateUser(token, request, response)) {
                return;
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            sendErrorResponse(response, "Error validating token: " + e.getMessage());
        }
    }

    private boolean shouldSkipFilter(String path) {
        return path.startsWith("/h2-console") || path.startsWith("/swagger") || path.startsWith("/v3/api-docs");
    }

    private String extractToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }
        return authorizationHeader.replace("Bearer ", "");
    }

    private boolean authenticateUser(String token, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long userId = Long.parseLong(jwtTokenValidator.getUserId(token));
        Optional<ApiResponse<Boolean>> userExists = userClient.isUserExists(userId);

        if (userExists.isEmpty() || !Boolean.TRUE.equals(userExists.get().getData())) {
            log.warn("User with id {} does not exist, or service is unavailable", userId);
            sendErrorResponse(response, "Invalid token data provided, unable to authenticate");
            return false;
        }

        var customUserPrincipal = new CustomUserPrincipal(userId);
        var authentication = new UsernamePasswordAuthenticationToken(customUserPrincipal, null, null);

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return true;
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(message);
    }
}
