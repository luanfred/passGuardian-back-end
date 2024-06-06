package com.passGuardian.passGuardian.dtos;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public record UpdatePasswordDto(
        String token,
        String password
) {
    public UpdatePasswordDto {
        token = token.strip();
        password = new BCryptPasswordEncoder().encode(password.strip());
    }
}
