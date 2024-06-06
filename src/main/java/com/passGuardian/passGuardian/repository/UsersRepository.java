package com.passGuardian.passGuardian.repository;

import com.passGuardian.passGuardian.models.UsersModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<UsersModel, Long> {
    Optional<UsersModel> findByUsername(String username);
}
