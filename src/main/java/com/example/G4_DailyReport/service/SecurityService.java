package com.example.G4_DailyReport.service;

import com.example.G4_DailyReport.enums.Role;
import com.example.G4_DailyReport.model.User;
import com.example.G4_DailyReport.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    public void updateUser(User user, String password, Role role) {
        user.setPassword(encoder.encode(password));
        user.setRole(role);
        user.setRole(role);
    }

    @Transactional
    public void grantRoles() {

        //Encode password into BScript type
        Optional<User> user = userRepository.findByUserName("user");
        user.ifPresent(value -> {
            updateUser(value, "123", Role.ROLE_USER);
            userRepository.save(value);
        });

        Optional<User> manager = userRepository.findByUserName("MANAGER");
        manager.ifPresent(value -> {
            updateUser(value, "123", Role.ROLE_MANAGER);
            userRepository.save(value);
        });

        Optional<User> admin = userRepository.findByUserName("ADMIN");
        admin.ifPresent(value -> {
            updateUser(value, "123", Role.ROLE_ADMIN);
            userRepository.save(value);
        });

        userRepository.flush();
    }

}
