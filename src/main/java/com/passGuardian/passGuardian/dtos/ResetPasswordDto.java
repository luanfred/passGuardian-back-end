package com.passGuardian.passGuardian.dtos;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordDto(
        @NotBlank
        String username
) {
}
