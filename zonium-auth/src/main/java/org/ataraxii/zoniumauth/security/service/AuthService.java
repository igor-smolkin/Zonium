package org.ataraxii.zoniumauth.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ataraxii.zoniumauth.database.entity.RefreshToken;
import org.ataraxii.zoniumauth.database.entity.Role;
import org.ataraxii.zoniumauth.database.entity.RoleType;
import org.ataraxii.zoniumauth.database.entity.User;
import org.ataraxii.zoniumauth.database.repository.RefreshTokenRepository;
import org.ataraxii.zoniumauth.database.repository.RoleRepository;
import org.ataraxii.zoniumauth.database.repository.UserRepository;
import org.ataraxii.zoniumauth.exception.ConflictException;
import org.ataraxii.zoniumauth.exception.InvalidTokenException;
import org.ataraxii.zoniumauth.exception.NotFoundException;
import org.ataraxii.zoniumauth.security.dto.login.RequestLoginDto;
import org.ataraxii.zoniumauth.security.dto.login.ResponseLoginDto;
import org.ataraxii.zoniumauth.security.dto.register.RequestRegisterDto;
import org.ataraxii.zoniumauth.security.dto.register.ResponseRegisterDto;
import org.ataraxii.zoniumauth.security.dto.token.RequestRefreshDto;
import org.ataraxii.zoniumauth.security.dto.token.ResponseRefreshDto;
import org.ataraxii.zoniumauth.security.mapper.AuthMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthMapper authMapper;

    public ResponseRegisterDto register(RequestRegisterDto request) {
        log.info("Попытка регистрации: username={}", request.getEmail());
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Ошибка регистрации: пользователь с почтой '{}' уже существует", request.getEmail());
            throw new ConflictException("Пользователь с такой почтой уже существует");
        }

        Role userRole = roleRepository.findByName(RoleType.USER)
                .orElseThrow(() -> new NotFoundException("Роль USER не найдена"));

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .email(request.getEmail())
                .password(hashedPassword)
                .isEnabled(true)
                .createdAt(Instant.now())
                .roles(Set.of(userRole))
                .build();

        userRepository.save(user);

        log.info("Пользователь '{}' успешно зарегистрирован", user.getEmail());

        return authMapper.toRegisterDto(user);
    }

    public ResponseLoginDto login(RequestLoginDto request) {
        try {
            log.info("Попытка входа: email='{}'", request.getEmail());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            User user = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

            List<RoleType> roles = user.getRoles().stream()
                    .map(Role::getName)
                    .toList();

            String accessToken = jwtService.generateAccessToken(user.getEmail(), user.getId(), roles);
            String refreshToken = jwtService.generateRefreshToken(user.getEmail());

            Date expiryDate = jwtService.getRefreshTokenExpiryDate();

            refreshTokenRepository.save(
                    RefreshToken.builder()
                            .user(user)
                            .token(refreshToken)
                            .expiryDate(expiryDate.toInstant())
                            .build()
            );

            log.info("Успешный вход: email='{}'", request.getEmail());
            return new ResponseLoginDto(accessToken, refreshToken);
        } catch (BadCredentialsException e) {
            log.warn("Ошибка входа: email='{}' неверный логин или пароль", request.getEmail());
            throw e;
        } catch (DisabledException e) {
            log.warn("Ошибка входа: email='{}' пользователь деактивирован", request.getEmail());
            throw e;
        }
    }

    public ResponseRefreshDto refresh(RequestRefreshDto request) {
        if (!jwtService.validateToken(request.getRefreshToken())) {
            throw new InvalidTokenException("Refresh токен недействителен");
        }

        RefreshToken storedToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new InvalidTokenException("Refresh токен не найден"));

        String email = jwtService.extractUsername(request.getRefreshToken());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        List<RoleType> roles = user.getRoles().stream()
                .map(Role::getName)
                .toList();

        String accessToken = jwtService.generateAccessToken(email, user.getId(), roles);
        String refreshToken = jwtService.generateRefreshToken(email);

        storedToken.setToken(refreshToken);
        storedToken.setExpiryDate(calculateExpiry());
        refreshTokenRepository.save(storedToken);

        return new ResponseRefreshDto(accessToken, refreshToken);
    }

    public Instant calculateExpiry() {
        Date date = jwtService.getRefreshTokenExpiryDate();
        return date.toInstant();
    }
}
