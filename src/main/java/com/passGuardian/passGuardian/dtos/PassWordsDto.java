package com.passGuardian.passGuardian.dtos;

import com.passGuardian.passGuardian.models.UsersModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public record PassWordsDto(
        @NotBlank
        String title,
        String url,
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotBlank
        String favorite,
        @NotNull
        Long user_id
) {
    public PassWordsDto {
        if (favorite == null || favorite.isBlank()) {
            favorite = "N";
        } else if (!favorite.equals("S") && !favorite.equals("N")) {
            throw new IllegalArgumentException("Favorite must be 'S' or 'N'");
        }
    }
}
