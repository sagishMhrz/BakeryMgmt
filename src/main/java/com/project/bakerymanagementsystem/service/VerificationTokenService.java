package com.project.bakerymanagementsystem.service;

import com.project.bakerymanagementsystem.entity.VerificationToken;
import com.project.bakerymanagementsystem.entity.Employee;
import com.project.bakerymanagementsystem.repository.UserRepository;
import com.project.bakerymanagementsystem.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VerificationTokenService {
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private UserRepository userRepository;

    private static final int OTP_EXPIRATION_MINUTES = 5;
    public VerificationToken createVerificationToken(Employee employee, String otp) {
        VerificationToken token = new VerificationToken();
        token.setEmployee(employee);
        token.setToken(otp);
        token.setCreatedDate(LocalDateTime.now());
        token.setExpiryDate(LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES));
        return verificationTokenRepository.save(token);
    }

    public VerificationToken getToken(String otp) {
        VerificationToken token = verificationTokenRepository.findByToken(otp);
        if (token != null && isTokenExpired(token)) {
            deleteToken(token);
            return null;
        }
        return token;
    }

    boolean isTokenExpired(VerificationToken token) {
        return LocalDateTime.now().isAfter(token.getCreatedDate().plusMinutes(OTP_EXPIRATION_MINUTES));
    }

    public void deleteToken(VerificationToken token) {
        verificationTokenRepository.delete(token);
    }
}
