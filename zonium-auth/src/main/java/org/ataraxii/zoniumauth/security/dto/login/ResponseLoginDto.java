package org.ataraxii.zoniumauth.security.dto.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ResponseLoginDto {
    private String accessToken;
    private String refreshToken;
}
