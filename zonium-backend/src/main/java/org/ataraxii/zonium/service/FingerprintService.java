package org.ataraxii.zonium.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ataraxii.zonium.database.entity.BrowserSession;
import org.ataraxii.zonium.database.repository.BrowserSessionRepository;
import org.ataraxii.zonium.dto.fingerprint.RequestFingerprintDto;
import org.ataraxii.zonium.dto.session.ResponseSessionDto;
import org.ataraxii.zonium.exception.NotFoundException;
import org.ataraxii.zonium.mapper.SessionMapper;
import org.ataraxii.zonium.security.SecurityUtil;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FingerprintService {

    private final BrowserSessionRepository sessionRepository;
    private final SessionMapper sessionMapper;
    private final SecurityUtil securityUtil;

    @Transactional
    public ResponseSessionDto update(Long id, RequestFingerprintDto dto, UUID userId) {
        String email = securityUtil.getCurrentEmail();
        BrowserSession session = sessionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException("Сессия не найдена"));

        log.info("Присвоение fingerprint и User-Agent сессии '{}' пользователем '{}'", id, email);

        if (dto.getFingerprint() != null) session.setFingerprint(dto.getFingerprint());
        if (dto.getUserAgent() != null) session.setUserAgent(dto.getUserAgent());

        sessionRepository.save(session);
        log.info("fingerprint и User-Agent сессии '{}' пользователем '{}' успешно присвоен", id, email);

        return sessionMapper.toDto(session);
    }
}
