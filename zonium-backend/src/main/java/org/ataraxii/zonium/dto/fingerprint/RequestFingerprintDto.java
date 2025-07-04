package org.ataraxii.zonium.dto.fingerprint;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestFingerprintDto {
    private String fingerprint;
    private String userAgent;
}
