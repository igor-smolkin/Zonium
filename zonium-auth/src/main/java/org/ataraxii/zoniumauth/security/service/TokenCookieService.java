package org.ataraxii.zoniumauth.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenCookieService {

    private final JwtService jwtService;

    public ResponseCookie createAccessCookie(String accessToken) {
        return ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(jwtService.getAccessTokenValiditySeconds())
                .sameSite("Strict")
                .build();
    }

    public ResponseCookie createRefreshCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true) // При выкатывании на прод сделать true (!!)
                .path("/")
                .maxAge(jwtService.getRefreshTokenValiditySeconds())
                .sameSite("Strict") // При выкатывании на прод сделать Strict (!!)
                .build();
    }
}
