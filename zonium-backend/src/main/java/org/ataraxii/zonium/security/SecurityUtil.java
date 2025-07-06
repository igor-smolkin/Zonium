package org.ataraxii.zonium.security;

import org.ataraxii.zonium.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityUtil {

    public UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof JwtUser user)) {
            throw new UnauthorizedException("Пользователь не аутентифицирован");
        }
        return user.getUserId();
    }

    public String getCurrentEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof JwtUser user)) {
            throw new UnauthorizedException("Пользователь не аутентифицирован");
        }
        return user.getUsername();
    }
}
