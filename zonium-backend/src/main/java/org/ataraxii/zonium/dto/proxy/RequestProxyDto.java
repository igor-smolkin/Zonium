package org.ataraxii.zonium.dto.proxy;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestProxyDto {
    private String name;
    private String host;
    private Integer port;
    private String username;
    private String password;
}
