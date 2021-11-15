package com.incentro.myservice.users.respository;

import com.incentro.myservice.users.entity.UserPasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPasswordResetRepository extends JpaRepository<UserPasswordReset, Long> {

    Optional<UserPasswordReset> findByToken(String token);
}
