package org.ataraxii.zonium.mapper;

import lombok.Builder;
import lombok.Data;
import org.ataraxii.zonium.database.entity.Proxy;
import org.ataraxii.zonium.dto.proxy.ResponseProxyDto;
import org.springframework.stereotype.Component;

@Component
@Data
@Builder
public class ProxyMapper {

    public ResponseProxyDto toDto(Proxy proxy) {
        return ResponseProxyDto.builder()
                .id(proxy.getId())
                .name(proxy.getName())
                .host(proxy.getHost())
                .port(String.valueOf(proxy.getPort()))
                .username(proxy.getUsername())
                .password(proxy.getPassword())
                .createdAt(proxy.getCreatedAt())
                .build();
    }
}
