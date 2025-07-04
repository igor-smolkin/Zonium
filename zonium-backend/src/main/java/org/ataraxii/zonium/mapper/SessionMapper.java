package org.ataraxii.zonium.mapper;

import org.ataraxii.zonium.database.entity.BrowserSession;
import org.ataraxii.zonium.dto.session.ResponseSessionDto;
import org.springframework.stereotype.Component;

@Component
public class SessionMapper {

    public ResponseSessionDto toDto(BrowserSession session) {
        return ResponseSessionDto.builder()
                .id(session.getId())
                .proxyId(session.getProxy().getId())
                .fingerprint(session.getFingerprint())
                .userAgent(session.getUserAgent())
                .status(session.getStatus())
                .createdAt(session.getCreatedAt())
                .build();
    }
}
