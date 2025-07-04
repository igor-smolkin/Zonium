package org.ataraxii.zonium.dto.session;

import lombok.Builder;
import lombok.Data;
import org.ataraxii.zonium.database.entity.Status;

import java.time.Instant;

@Data
@Builder
public class ResponseSessionDto {
    private Long id;
    private Long proxyId;
    private String fingerprint;
    private String userAgent;
    private Status status;
    private Instant createdAt;
}
