package org.ataraxii.zoniumauth.security.dto.register;

import lombok.Builder;
import lombok.Data;
import org.ataraxii.zoniumauth.database.entity.Role;
import org.ataraxii.zoniumauth.database.entity.RoleType;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class ResponseRegisterDto {
    private UUID id;
    private String email;
    private Set<Role> roles;
}
