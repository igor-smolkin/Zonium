package org.ataraxii.zonium.dto.proxy;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ResponseProxyDto {
    private Long id;
    private String name;
    private String host;
    private String port;
    private String username;
    private String password;
    private Instant createdAt;
}
