package com.example.G4_DailyReport.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.G4_DailyReport.model.Project;
import com.example.G4_DailyReport.model.ProjectMember;
import com.example.G4_DailyReport.model.User;
import com.example.G4_DailyReport.repository.ProjectMemberRepository;
import com.example.G4_DailyReport.repository.ProjectRepository;
import com.example.G4_DailyReport.repository.UserRepository;
import com.example.G4_DailyReport.util.PagingUtil;
@Service
public class ProjectMemberService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private ProjectMemberRepository projectMemberRepository;   
	public User getUserById(String userId) {
		return userRepository.findById(UUID.fromString(userId)).orElse(null);
	}
	public User getUserByUserUName(String userName) {
		Optional<User> userByUName = userRepository.findByUserName(userName);
		
		return userByUName.get();
	}

	public List<Project> getAllProjectOfUser(String userId) {
		User user = getUserById(userId);
		return user.getProjectMembers().stream().map(item -> item.getProject()).collect(Collectors.toList());
	}

	public Page<User> getAllUserOfProject(String name, int page, String projectId) {
		List<ProjectMember> projectMembers = projectRepository.findProjectById(UUID.fromString(projectId))
				.getProjectMembers();
		List<User> users = projectMembers.stream().map(item -> item.getUser()).collect(Collectors.toList());
		List<User> usersByName = (name.isEmpty() ? users
				: users.stream().filter(item -> item.getName().toLowerCase().contains(name.toLowerCase()))
						.collect(Collectors.toList()));
		Page<User> pageUsers = PagingUtil.getPage(usersByName, page);
		return pageUsers;
	}

	public Page<User> getAllUserNotOfProject(String name, String projectId, int page) {
		List<User> users = userRepository.findAll().stream().filter(
				item -> item.getProjectMembers().stream().noneMatch(obj -> !obj.getId().toString().equals(projectId)))
				.collect(Collectors.toList());
		
		List<User> usersByName = (name.isEmpty() ? users
				: users.stream().filter(item -> item.getName().toLowerCase().contains(name.toLowerCase()))
						.collect(Collectors.toList()));
		Page<User> pageUsers = PagingUtil.getPage(usersByName, page);
		return pageUsers;
	}

	public int addMember(String userId, String projectId) {
		User user = getUserById(userId);
		Project project = projectRepository.findProjectById(UUID.fromString(projectId));
		List<User> users = userRepository.findAll().stream().filter(
				item -> item.getProjectMembers().stream().noneMatch(obj -> !obj.getId().toString().equals(projectId)))
				.collect(Collectors.toList());

		int page = PagingUtil.calculatePageNumber(users, user.getId());
		ProjectMember projectMember = new ProjectMember();
		projectMember.setUser(user);
		projectMember.setProject(project);
		projectMemberRepository.save(projectMember);

		return page;

	}

	public int deleteMember(String userId, String projectId) {
		Optional<ProjectMember> projectMemberOpt = projectMemberRepository.findAll().stream()
				.filter(item -> (item.getUser().getId().equals(UUID.fromString(userId))
						&& item.getProject().getId().equals(UUID.fromString(projectId))))
				.findFirst();
		List<User> users = userRepository.findAll().stream().filter(
				item -> item.getProjectMembers().stream().noneMatch(obj -> obj.getId().toString().equals(projectId)))
				.collect(Collectors.toList());
		int currentPage = PagingUtil.calculatePageNumber(users, UUID.fromString(userId));
		projectMemberRepository.delete(projectMemberOpt.get());
		return currentPage;
	}
}
