package com.example.G4_DailyReport.service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.example.G4_DailyReport.model.Department;
import com.example.G4_DailyReport.model.User;
import com.example.G4_DailyReport.repository.DepartmentRepository;
import com.example.G4_DailyReport.repository.UserRepository;
import com.example.G4_DailyReport.util.PagingUtil;

@Service
public class ManagerService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private DepartmentRepository departmentRepository;
	public User getUserById(String userId) {
		return userRepository.findById(UUID.fromString(userId)).orElse(null);
	}
	public User getUserByUserUName(String userName) {
		Optional<User> userByUName = userRepository.findByUserName(userName);
		
		return userByUName.get();
	}

	public Page<User> getAllUserInDepartment(String name, String departmentId, int page) {
		Optional<Department> department = departmentRepository.findById(UUID.fromString(departmentId));
		List<User> users = userRepository.findByDepartment(department.get());
		List<User> usersByName = name.isEmpty() ? users
				: users.stream().filter(user -> user.getName().toLowerCase().contains(name.toLowerCase()))
						.collect(Collectors.toList());
		Page<User> pageUsers = PagingUtil.getPage(usersByName, page);
		return pageUsers.isEmpty() ? Page.empty() : pageUsers;
	}

	public Page<User> getAllUserNoDepartment(String name, int page) {
		List<User> users = userRepository.findByDepartmentIsNull();
		List<User> usersByName = name.isEmpty() ? users
				: users.stream().filter(user -> user.getName().toLowerCase().contains(name.toLowerCase()))
						.collect(Collectors.toList());
		Page<User> pageUsers = PagingUtil.getPage(usersByName, page);
		return pageUsers.isEmpty() ? Page.empty() : pageUsers;

	}
	public int save(User user) {
		int page=PagingUtil.calculatePageNumber(userRepository.findByDepartmentIsNull(), user.getId());
		userRepository.save(user);
		return page;
	}
	
	public int delete(User user) {
		int page=PagingUtil.calculatePageNumber(userRepository.findByDepartment(user.getDepartment()), user.getId());
		user.setDepartment(null);
		userRepository.save(user);
		return page;
	}



}
