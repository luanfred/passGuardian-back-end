package com.passGuardian.passGuardian.dtos;

public record PassWordResponseDto(
        long password_id,
        String title,
        String url,
        String email,
        String password,
        String favorite
) {}
