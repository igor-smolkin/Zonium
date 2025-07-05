package org.ataraxii.zoniumauth.security.mapper;

import org.ataraxii.zoniumauth.database.entity.User;
import org.ataraxii.zoniumauth.security.dto.register.ResponseRegisterDto;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {
    public ResponseRegisterDto toRegisterDto(User user) {
        return ResponseRegisterDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }
}
