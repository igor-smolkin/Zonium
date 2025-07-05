package org.ataraxii.zoniumauth.security.dto.login;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestLoginDto {
    private String email;
    private String password;
}
