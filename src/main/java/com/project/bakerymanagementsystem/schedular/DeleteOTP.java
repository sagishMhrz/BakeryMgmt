package com.project.bakerymanagementsystem.schedular;

import com.project.bakerymanagementsystem.entity.VerificationToken;
import com.project.bakerymanagementsystem.entity.Employee;
import com.project.bakerymanagementsystem.repository.UserRepository;
import com.project.bakerymanagementsystem.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeleteOTP {
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Scheduled(cron = "0 */1 * * * ?")
    public void cleanupExpiredTokens() {

        LocalDateTime now = LocalDateTime.now();
        List<VerificationToken> expiredTokens = verificationTokenRepository.findByExpiryDateBefore(now);

        for (VerificationToken token : expiredTokens) {
            verificationTokenRepository.delete(token);
            Employee employee = token.getEmployee();
            userRepository.delete(employee);
        }
    }
}
