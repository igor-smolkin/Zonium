package org.ataraxii.zoniumauth.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ataraxii.zoniumauth.exception.InvalidTokenException;
import org.ataraxii.zoniumauth.security.service.CustomUserDetailsService;
import org.ataraxii.zoniumauth.security.service.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String jwt = resolveToken(request);
        String username = null;

        if (jwt != null) {
            try {
                username = jwtService.extractUsername(jwt);
            } catch (Exception e) {
                log.warn("Ошибка чтения токена");
                throw new InvalidTokenException("Некорректный токен");
            }
        }

        // SecurityContextHolder проверяет что в текущем контексте никто еще не аутентифицирован
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Передаем информацию о пользователе principal, credentials и authorities
                // В данном случае не передаем пароль так как его проверка выполняется на уровне JWT
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                // Прикрепление дополнительной информации о пользователе
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                // Добавляем в контекст информацию о том что мы аутентифицировали пользователя по токену
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    // Извлечение токена из заголовка или куки (временно опционально, в дальнейшем возможен пересмотр)
    private String resolveToken(HttpServletRequest request) {
        // Логика для обработки заголовков
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        // Логика для обработки куки
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
