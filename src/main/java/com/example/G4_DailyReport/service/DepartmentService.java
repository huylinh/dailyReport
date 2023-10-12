package com.example.G4_DailyReport.service;

import com.example.G4_DailyReport.enums.Role;
import com.example.G4_DailyReport.model.Department;
import com.example.G4_DailyReport.model.User;
import com.example.G4_DailyReport.repository.DepartmentRepository;
import com.example.G4_DailyReport.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private UserRepository userRepository;

    public Page<Department> findAll(String search, Pageable pageable) {
        Specification<Department> spec = Specification.where(
                        (root, query, criteriaBuilder) -> criteriaBuilder.or(
                                criteriaBuilder.like(root.get("name"), "%" + search + "%"),
                                criteriaBuilder.like(root.get("description"), "%" + search + "%")
                ));

        Page<Department> pagedResult = departmentRepository.findAll(
                spec,
                pageable
        );
        if (pagedResult.hasContent()) {
            return pagedResult;
        }
        return Page.empty();
    }

    public Department findDepartmentAndManagers(UUID id) {
        return departmentRepository.findById(id).orElse(null);
    }

    public Department findById(UUID id) {
        return departmentRepository.findById(id).orElse(null);
    }

    public Department update(Department department) {
        return departmentRepository.save(department);
    }

    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    public void deleteById(UUID id) {
        departmentRepository.deleteById(id);
    }

    public Page<User> findManagers(UUID id, Pageable pageable) {
//        return userRepository.findAllByDepartmentIdAndPositionName(id, "Manager", pageable);
        return userRepository.findAllByDepartmentIdAndRole(id, Role.ROLE_MANAGER, pageable);
    }

    public void addManager(UUID departmentId, UUID managerId) {
        User manager = userRepository.findById(managerId).orElse(null);
        Department department = departmentRepository.findById(departmentId).orElse(null);

        this.throwIfDepartmentAndManagerIsNull(department, manager);
        this.throwIfManagerHaveDepartment(manager);

        manager.setDepartment(department);
        userRepository.save(manager);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addManagers(UUID departmentId, Iterable<UUID> managerIds) {
        for(UUID managerId : managerIds) {
            addManager(departmentId, managerId);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void removeManager(UUID departmentId, UUID managerId) {
        User manager = userRepository.findById(managerId).orElse(null);
        Department department = departmentRepository.findById(departmentId).orElse(null);

        this.throwIfDepartmentAndManagerIsNull(department, manager);
        this.throwIfManagerNotHaveDepartment(manager);
        this.throwIfManagerNotBelongToDepartment(manager, departmentId);

        manager.setDepartment(null);
        userRepository.save(manager);
    }

    private void throwIfDepartmentAndManagerIsNull(Department department, User manager) throws DataIntegrityViolationException {
        if(manager == null || department == null) {
            throw new DataIntegrityViolationException("Manager or Department not found");
        }
    }

    private void throwIfManagerNotHaveDepartment(User manager) throws DataIntegrityViolationException {
        if(manager.getDepartment() == null) {
            throw new DataIntegrityViolationException("Manager does not have a department");
        }
    }

    private void throwIfManagerHaveDepartment(User manager) throws DataIntegrityViolationException {
        if(manager.getDepartment() != null) {
            throw new DataIntegrityViolationException("Manager does not have a department");
        }
    }

    private void throwIfManagerNotBelongToDepartment(User manager, UUID departmentId) throws DataIntegrityViolationException {
        if(!manager.getDepartment().getId().equals(departmentId)) {
            throw new DataIntegrityViolationException("Manager does not belong to this department");
        }
    }
}