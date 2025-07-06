package org.ataraxii.zoniumauth.security.mapper;

import org.ataraxii.zoniumauth.database.entity.Role;
import org.ataraxii.zoniumauth.database.entity.RoleType;
import org.ataraxii.zoniumauth.database.entity.User;
import org.ataraxii.zoniumauth.security.dto.register.ResponseRegisterDto;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AuthMapper {
    public ResponseRegisterDto toRegisterDto(User user) {
        Set<RoleType> roleTypes = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return ResponseRegisterDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .roles(roleTypes)
                .build();
    }
}
