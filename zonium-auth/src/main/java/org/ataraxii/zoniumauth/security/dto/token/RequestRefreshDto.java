package org.ataraxii.zoniumauth.security.dto.token;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestRefreshDto {
    private String refreshToken;
}
