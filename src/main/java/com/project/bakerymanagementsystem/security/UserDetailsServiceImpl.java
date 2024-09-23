package com.project.bakerymanagementsystem.security;

import com.project.bakerymanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import com.project.bakerymanagementsystem.entity.Employee;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // work with email
        Optional<Employee> admin = userRepository.findByEmail(email);
        if (!admin.get().isEnabled()) {
            throw new DisabledException("Employee is not enabled to log in");
        }
        return admin.map(CustomUserDetails::new)
                .orElseThrow(()-> new UsernameNotFoundException("admin not found "+ email));
    }
}
