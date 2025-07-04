package org.ataraxii.zonium.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ataraxii.zonium.database.entity.BrowserSession;
import org.ataraxii.zonium.database.repository.BrowserSessionRepository;
import org.ataraxii.zonium.dto.fingerprint.RequestFingerprintDto;
import org.ataraxii.zonium.dto.session.ResponseSessionDto;
import org.ataraxii.zonium.exception.NotFoundException;
import org.ataraxii.zonium.mapper.SessionMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FingerprintService {

    private final BrowserSessionRepository sessionRepository;
    private final SessionMapper sessionMapper;

    @Transactional
    public ResponseSessionDto update(Long id, RequestFingerprintDto dto) {
        BrowserSession session = sessionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Session not found"));

        if (dto.getFingerprint() != null) session.setFingerprint(dto.getFingerprint());
        if (dto.getUserAgent() != null) session.setUserAgent(dto.getUserAgent());

        sessionRepository.save(session);

        return sessionMapper.toDto(session);
    }
}
