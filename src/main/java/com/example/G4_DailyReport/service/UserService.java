package com.example.G4_DailyReport.service;

import com.example.G4_DailyReport.enums.Role;
import com.example.G4_DailyReport.model.User;
import com.example.G4_DailyReport.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Page<User> findAllManagerByDepartmentId(UUID department, Pageable pageable) {
        return userRepository.findAllByDepartmentIdAndRole(department, Role.ROLE_MANAGER, pageable);
    }

    public Page<User> findAllManagerByDepartmentIsNull(Pageable pageable) {
        return userRepository.findAllByDepartmentIsNullAndRole(Role.ROLE_MANAGER, pageable);
    }

    public Page<User> findAllManagerByDepartmentIsNull(String name, Pageable pageable) {
        return userRepository.findAllByDepartmentIsNullAndRoleAndNameContaining(Role.ROLE_MANAGER, name, pageable);
    }
}
