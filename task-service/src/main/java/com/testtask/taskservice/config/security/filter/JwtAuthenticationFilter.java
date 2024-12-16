package com.testtask.taskservice.config.security.filter;

import com.testtask.taskservice.config.security.jwt.JwtTokenValidator;
import com.testtask.taskservice.model.dto.response.ApiResponse;
import com.testtask.taskservice.model.security.CustomUserPrincipal;
import com.testtask.taskservice.service.user.UserClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String path = request.getRequestURI();
        if (path.startsWith("/h2-console") || path.startsWith("/swagger") || path.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid Authorization header");
            return;
        }

        String token = authorizationHeader.replace("Bearer ", "");
        try {
            if (jwtTokenValidator.validateToken(token)) {
                Long userId = Long.parseLong(jwtTokenValidator.getUserId(token));

                Optional<ApiResponse<Boolean>> userExists = userClient.isUserExists(userId);

                if (userExists.isEmpty() || !Boolean.TRUE.equals(userExists.get().getData())) {
                    log.warn("User with id {} does not exist, or service is unavailable", userId);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Invalid token data provided, unable to authenticate");
                }

                CustomUserPrincipal customUserPrincipal = new CustomUserPrincipal(userId);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(customUserPrincipal, null, null);

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.warn("Token is not valid, unable to authenticate");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid token");
                return;
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Error validating token: " + e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }
}
