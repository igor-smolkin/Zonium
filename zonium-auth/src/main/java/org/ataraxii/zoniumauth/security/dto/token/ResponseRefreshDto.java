package org.ataraxii.zoniumauth.security.dto.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ResponseRefreshDto {
    private String accessToken;
    private String refreshToken;
}
