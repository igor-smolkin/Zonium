package org.ataraxii.zoniumauth.security.service;

import lombok.RequiredArgsConstructor;
import org.ataraxii.zoniumauth.database.repository.UserRepository;
import org.ataraxii.zoniumauth.exception.NotFoundException;
import org.ataraxii.zoniumauth.security.adapter.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new NotFoundException("Пользователь с такой почтой не найден"));
    }
}
