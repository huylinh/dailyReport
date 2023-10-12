package com.example.G4_DailyReport.repository;

import com.example.G4_DailyReport.dto.UsernameAndId;
import com.example.G4_DailyReport.enums.Role;

import com.example.G4_DailyReport.model.Department;
import com.example.G4_DailyReport.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserRepository extends JpaRepository<User, UUID> {    
    List<User> findByDepartmentIsNull();
    List<User> findByDepartment(Department department);
    @Query("SELECT pm.user FROM ProjectMember pm " +
           "JOIN pm.project p " +
           "JOIN pm.user u " +
           "WHERE p.id = ?1 " +
           "AND u.role = ?2 ")
    List<User> findAllMemberInProjectByProjectIdAndRole(UUID projectId, Role role);
    @Query(value = "select new com.example.G4_DailyReport.dto.UsernameAndId(u.id, u.userName) from User u WHERE u.userName in ?1")
    List<UsernameAndId> findAllUserIdByUserName(Set<String> UserName);
    Optional<User> findByUserName(String username);
    Page<User> findAllByDepartmentIdAndRole(UUID department, Role role, Pageable pageable);
    Page<User> findAllByDepartmentIsNullAndRole(Role role, Pageable pageable);
    Page<User> findAllByDepartmentIsNullAndRoleAndNameContaining(Role role, String name, Pageable pageable);
}
