package org.ataraxii.zonium.dto.session;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestSessionDto {
    private Long proxyId;
}
