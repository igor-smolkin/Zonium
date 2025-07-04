package org.ataraxii.zonium.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ataraxii.zonium.database.entity.Proxy;
import org.ataraxii.zonium.database.repository.ProxyRepository;
import org.ataraxii.zonium.dto.proxy.RequestProxyDto;
import org.ataraxii.zonium.dto.proxy.ResponseProxyDto;
import org.ataraxii.zonium.exception.NotFoundException;
import org.ataraxii.zonium.mapper.ProxyMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProxyService {

    private final ProxyRepository proxyRepository;
    private final ProxyMapper proxyMapper;

    @Transactional
    public ResponseProxyDto create(RequestProxyDto dto) {

        Proxy proxy = Proxy.builder()
                .name(dto.getName())
                .host(dto.getHost())
                .port(dto.getPort())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .build();

        proxyRepository.save(proxy);

        return proxyMapper.toDto(proxy);
    }

    public List<ResponseProxyDto> findAll() {
        List<Proxy> proxies = proxyRepository.findAll();
        return proxies.stream()
                .map(proxyMapper::toDto)
                .collect(Collectors.toList());
    }

    public ResponseProxyDto findById(Long id) {
        Proxy proxy = proxyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Proxy not found"));
        return proxyMapper.toDto(proxy);
    }

    @Transactional
    public ResponseProxyDto update(Long id, RequestProxyDto dto) {
        Proxy proxy = proxyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Proxy not found"));

        if (dto.getName() != null) proxy.setName(dto.getName());
        if (dto.getHost() != null) proxy.setHost(dto.getHost());
        if (dto.getPort() != null) proxy.setPort(dto.getPort());
        if (dto.getUsername() != null) proxy.setUsername(dto.getUsername());
        if (dto.getPassword() != null) proxy.setPassword(dto.getPassword());

        proxyRepository.save(proxy);
        return proxyMapper.toDto(proxy);
    }

    @Transactional
    public void delete(Long id) {
        if (!proxyRepository.existsById(id)) {
            throw new NotFoundException("Proxy not found");
        }
        proxyRepository.deleteById(id);
    }
}
