package com.project.bakerymanagementsystem.repository;

import com.project.bakerymanagementsystem.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
    List<VerificationToken> findByExpiryDateBefore(LocalDateTime expiryDate);
}
