package org.ataraxii.zoniumauth.security.controller;

import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ataraxii.zoniumauth.security.dto.login.RequestLoginDto;
import org.ataraxii.zoniumauth.security.dto.login.ResponseLoginDto;
import org.ataraxii.zoniumauth.security.dto.register.RequestRegisterDto;
import org.ataraxii.zoniumauth.security.dto.register.ResponseRegisterDto;
import org.ataraxii.zoniumauth.security.dto.token.RequestRefreshDto;
import org.ataraxii.zoniumauth.security.dto.token.ResponseRefreshDto;
import org.ataraxii.zoniumauth.security.service.AuthService;
import org.ataraxii.zoniumauth.security.service.TokenCookieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final TokenCookieService tokenCookieService;

    @PostMapping("/register")
    public ResponseEntity<ResponseRegisterDto> register(@RequestBody RequestRegisterDto request) {
        ResponseRegisterDto response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseLoginDto> login(@RequestBody RequestLoginDto request) {
        ResponseLoginDto response = authService.login(request);

        ResponseCookie accessCookie = tokenCookieService.createAccessCookie(response.getAccessToken());
        ResponseCookie refreshCookie = tokenCookieService.createRefreshCookie(response.getRefreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString(), refreshCookie.toString())
                .build(); // Пока что собираем без тела ответа, только токены в куки
    }

    @PostMapping("/refresh")
    public ResponseEntity<ResponseRefreshDto> refresh(@RequestBody RequestRefreshDto request) {
        ResponseRefreshDto response = authService.refresh(request);

        ResponseCookie accessCookie = tokenCookieService.createAccessCookie(response.getAccessToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .build(); // При запросе обновления токена возвращаем только новый accessToken
    }
}
