package com.passGuardian.passGuardian.service;

import com.passGuardian.passGuardian.models.PasswordResetTokenModel;
import com.passGuardian.passGuardian.models.UsersModel;
import com.passGuardian.passGuardian.repository.PasswordResetTokenRepository;
import com.passGuardian.passGuardian.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;


    public void sendPasswordResetToken(String email) {
        UsersModel user = usersRepository.findByUsername(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        String token = UUID.randomUUID().toString().substring(0, 8);
        PasswordResetTokenModel passwordResetToken = new PasswordResetTokenModel(token, user);
        tokenRepository.save(passwordResetToken);
        try {
            emailService.sendEmail(user.getUsername(),
                    "Redefinição de Senha",
                    "Para redefinir sua senha, use o token: " + token);
        } catch (Exception e) {
            tokenRepository.delete(passwordResetToken);
            throw new RuntimeException("Erro ao enviar email" + e.getMessage());
        }
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetTokenModel passwordResetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido"));

        UsersModel user = passwordResetToken.getUser_id();
        user.setPassword(newPassword);
        usersRepository.save(user);
        tokenRepository.delete(passwordResetToken);
    }

    public boolean validateToken(String token) {
        var tokenResetPassword = tokenRepository.findByToken(token);
        if (tokenResetPassword.isEmpty()) {
            return false;
        } else {
            var DateToken = tokenResetPassword.get().getExpiryDate();
            var DateTimeCurrent = LocalDateTime.now();
            return DateToken.isAfter(DateTimeCurrent);
        }
    }
}
