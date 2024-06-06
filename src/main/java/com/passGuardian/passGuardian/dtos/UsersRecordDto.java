package com.passGuardian.passGuardian.dtos;

import jakarta.validation.constraints.NotBlank;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public record UsersRecordDto(
        @NotBlank
        String cpf,
        @NotBlank
        String name,
        @NotBlank
        String username,
        @NotBlank
        String password
) {
        public UsersRecordDto {
                password = new BCryptPasswordEncoder().encode(password.strip());
        }

}
