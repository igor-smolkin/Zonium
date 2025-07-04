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
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrowserSessionService {

    private final ProxyRepository proxyRepository;
    private final BrowserSessionRepository sessionRepository;
    private final SessionMapper sessionMapper;

    @Transactional
    public ResponseSessionDto create(RequestSessionDto dto) {
        log.info("Creating new browser session...");
        Proxy proxy = proxyRepository.findById(dto.getProxyId())
                .orElseThrow(() -> new NotFoundException("Proxy not found"));

        BrowserSession session = BrowserSession.builder()
                .proxy(proxy)
                .status(Status.CREATED)
                .createdAt(Instant.now())
                .build();

        sessionRepository.save(session);
        log.info("New browser session saved.");

        return sessionMapper.toDto(session);
    }

    public List<ResponseSessionDto> findAll() {
        List<BrowserSession> sessions = sessionRepository.findAll();
        return sessions.stream()
                .map(sessionMapper::toDto)
                .collect(Collectors.toList());
    }

    public ResponseSessionDto findById(Long id) {
        BrowserSession session = sessionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Session not found"));
        return sessionMapper.toDto(session);
    }

    @Transactional
    public ResponseSessionDto update (Long id, RequestSessionDto dto) {
        BrowserSession session = sessionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Session not found"));

        if (dto.getProxyId() != null) {
            Proxy proxy = proxyRepository.findById(dto.getProxyId())
                    .orElseThrow(() -> new NotFoundException("Proxy not found"));
            session.setProxy(proxy);
        }

        sessionRepository.save(session);

        return sessionMapper.toDto(session);
    }

    @Transactional
    public void delete (Long id) {
        if (!sessionRepository.existsById(id)) {
            throw new NotFoundException("Session not found");
        }
        sessionRepository.deleteById(id);
    }
}
