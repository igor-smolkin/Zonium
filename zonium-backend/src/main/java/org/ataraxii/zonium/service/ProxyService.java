package org.ataraxii.zonium.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ataraxii.zonium.database.entity.Proxy;
import org.ataraxii.zonium.database.repository.ProxyRepository;
import org.ataraxii.zonium.dto.proxy.RequestProxyDto;
import org.ataraxii.zonium.dto.proxy.ResponseProxyDto;
import org.ataraxii.zonium.exception.NotFoundException;
import org.ataraxii.zonium.mapper.ProxyMapper;
import org.ataraxii.zonium.security.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProxyService {

    private final ProxyRepository proxyRepository;
    private final ProxyMapper proxyMapper;
    private final SecurityUtil securityUtil;

    @Transactional
    public ResponseProxyDto create(RequestProxyDto dto, UUID userId) {
        String email = securityUtil.getCurrentEmail();
        log.info("Добавление прокси '{}' пользователем '{}'", dto.getHost(), email);

        Proxy proxy = Proxy.builder()
                .name(dto.getName())
                .host(dto.getHost())
                .port(dto.getPort())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .createdAt(Instant.now())
                .userId(userId)
                .build();

        proxyRepository.save(proxy);
        log.info("Прокси '{}' успешно добавлено пользователем '{}'", dto.getHost(), email);

        return proxyMapper.toDto(proxy);
    }

    public List<ResponseProxyDto> findAll(UUID userId) {
        List<Proxy> proxies = proxyRepository.findAllByUserId(userId);
        return proxies.stream()
                .map(proxyMapper::toDto)
                .collect(Collectors.toList());
    }

    public ResponseProxyDto findById(Long id, UUID userId) {
        Proxy proxy = proxyRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException("Прокси не найдено"));
        return proxyMapper.toDto(proxy);
    }

    @Transactional
    public ResponseProxyDto update(Long id, RequestProxyDto dto, UUID userId) {
        String email = securityUtil.getCurrentEmail();
        log.info("Обновление прокси '{}' пользователем '{}'", dto.getHost(), email);
        Proxy proxy = proxyRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException("Прокси не найдено"));

        if (dto.getName() != null) proxy.setName(dto.getName());
        if (dto.getHost() != null) proxy.setHost(dto.getHost());
        if (dto.getPort() != null) proxy.setPort(dto.getPort());
        if (dto.getUsername() != null) proxy.setUsername(dto.getUsername());
        if (dto.getPassword() != null) proxy.setPassword(dto.getPassword());

        proxyRepository.save(proxy);
        log.info("Прокси '{}' успешно обновлено пользователем '{}'", dto.getHost(), email);
        return proxyMapper.toDto(proxy);
    }

    @Transactional
    public void delete(Long id, UUID userId) {
        String email = securityUtil.getCurrentEmail();

        Proxy proxy = proxyRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException("Прокси не найдено"));

        proxyRepository.delete(proxy);
        log.info("Прокси '{}' удален пользователем '{}'", proxy.getHost(), email);
    }
}
