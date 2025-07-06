package org.ataraxii.zonium.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ataraxii.zonium.database.entity.BrowserSession;
import org.ataraxii.zonium.database.entity.Proxy;
import org.ataraxii.zonium.database.entity.Status;
import org.ataraxii.zonium.database.repository.BrowserSessionRepository;
import org.ataraxii.zonium.database.repository.ProxyRepository;
import org.ataraxii.zonium.dto.session.RequestSessionDto;
import org.ataraxii.zonium.dto.session.ResponseSessionDto;
import org.ataraxii.zonium.exception.NotFoundException;
import org.ataraxii.zonium.mapper.SessionMapper;
import org.ataraxii.zonium.security.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrowserSessionService {

    private final ProxyRepository proxyRepository;
    private final BrowserSessionRepository sessionRepository;
    private final SessionMapper sessionMapper;
    private final SecurityUtil securityUtil;

    @Transactional
    public ResponseSessionDto create(RequestSessionDto dto, UUID userId) {
        String email = securityUtil.getCurrentEmail();
        log.info("Создание новой сессии пользователем '{}'", email);

        Proxy proxy = proxyRepository.findByIdAndUserId(dto.getProxyId(), userId)
                .orElseThrow(() -> new NotFoundException("Прокси не найден"));

        BrowserSession session = BrowserSession.builder()
                .proxy(proxy)
                .status(Status.CREATED)
                .createdAt(Instant.now())
                .userId(userId)
                .build();

        sessionRepository.save(session);
        log.info("Сессия успешно создана пользователем '{}'", email);

        return sessionMapper.toDto(session);
    }

    public List<ResponseSessionDto> findAll(UUID userId) {
        List<BrowserSession> sessions = sessionRepository.findAllByUserId(userId);
        return sessions.stream()
                .map(sessionMapper::toDto)
                .collect(Collectors.toList());
    }

    public ResponseSessionDto findById(Long id, UUID userId) {
        BrowserSession session = sessionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException("Сессия не найдена"));
        return sessionMapper.toDto(session);
    }

    @Transactional
    public ResponseSessionDto update(Long id, RequestSessionDto dto, UUID userId) {
        String email = securityUtil.getCurrentEmail();
        BrowserSession session = sessionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException("Сессия не найдена"));

        log.info("Обновление сессии '{}' пользователем '{}'", id, email);

        if (dto.getProxyId() != null) {
            Proxy proxy = proxyRepository.findByIdAndUserId(dto.getProxyId(), userId)
                    .orElseThrow(() -> new NotFoundException("Прокси не найдено"));
            session.setProxy(proxy);
        }

        sessionRepository.save(session);
        log.info("Сессия '{}' успешно обновлена пользователем '{}'", id, email);
        return sessionMapper.toDto(session);
    }

    @Transactional
    public void delete(Long id, UUID userId) {
        String email = securityUtil.getCurrentEmail();

        BrowserSession session = sessionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException("Сессия не найдена"));

        sessionRepository.delete(session);
        log.info("Сессия '{}' удалена пользователем '{}'", id, email);
    }
}
