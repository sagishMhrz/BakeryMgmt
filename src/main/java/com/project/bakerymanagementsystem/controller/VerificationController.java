package com.project.bakerymanagementsystem.controller;

import com.project.bakerymanagementsystem.entity.VerificationToken;
import com.project.bakerymanagementsystem.entity.Employee;
import com.project.bakerymanagementsystem.repository.UserRepository;
import com.project.bakerymanagementsystem.service.UserService;
import com.project.bakerymanagementsystem.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VerificationController {
    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/verify-otp")
    public String verifyOtp(@RequestParam(value = "otp", required = false) String otp, Model model) {
        VerificationToken verificationToken = verificationTokenService.getToken(otp);

        if (verificationToken == null) {
            model.addAttribute("message", "Invalid or expired OTP");
            return "otpError";
        }

        Employee employee = verificationToken.getEmployee();
        employee.setEnabled(true);
        userRepository.save(employee);

        verificationTokenService.deleteToken(verificationToken);

        model.addAttribute("message", "OTP verified successfully. Your account is now active.");
        return "verified";
    }
    @GetMapping("/otp-entry")
    public String showOtpEntryPage() {
        return "otp-verification";  // This will look for otp-verification.html in the templates directory
    }
}
