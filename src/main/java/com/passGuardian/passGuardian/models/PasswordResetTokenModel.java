package com.passGuardian.passGuardian.models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Table(name = "password_reset_token")
@Entity(name = "password_reset_token")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class PasswordResetTokenModel {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 25)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UsersModel user_id;

    public PasswordResetTokenModel(String token, UsersModel user) {
        this.token = token;
        this.user_id = user;
        this.expiryDate = LocalDateTime.now().plusHours(1);
    }

    public PasswordResetTokenModel() {

    }
}
