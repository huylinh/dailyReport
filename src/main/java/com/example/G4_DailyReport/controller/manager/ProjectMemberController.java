package com.example.G4_DailyReport.controller.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.G4_DailyReport.model.Project;
import com.example.G4_DailyReport.model.User;
import com.example.G4_DailyReport.service.ProjectMemberService;
import com.example.G4_DailyReport.util.CurrentUserUtil;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager")
public class ProjectMemberController {
	@Autowired
	ProjectMemberService projectMemberService;
	//@Autowired
	//private ManagerSingleton managerSingleton;
	@ModelAttribute("user")
	public User loadCurrentUser() {
		return projectMemberService.getUserByUserUName(CurrentUserUtil.getCurrentUser().getUsername());
	}
    @PreAuthorize("hasRole('ROLE_MANAGER')")
	@GetMapping("/projects-management")
	public String managerProjectPage(Model model) {
		List<Project> projects = projectMemberService.getAllProjectOfUser(loadCurrentUser().getId().toString());
		model.addAttribute("projects", projects);
		return "managers/pages/projects-management";
	}
    
    @PreAuthorize("hasRole('ROLE_MANAGER')")
	@GetMapping("/project-management/{projectId}/members")
	public String managerMembers(@RequestParam(name="query",defaultValue = "") String query,@PathVariable String projectId,@RequestParam(defaultValue = "0") int page,Model model) {
		model.addAttribute("usersPage", projectMemberService.getAllUserOfProject(query,page, projectId));
		model.addAttribute("projectId",projectId);
		model.addAttribute("queryName",query);
		return "managers/pages/members-in-project-management";
	}
    
    @PreAuthorize("hasRole('ROLE_MANAGER')")
	@GetMapping("/project-management/{projectId}/addMembers")
	public String managerAddMember(@RequestParam(defaultValue = "") String query,@PathVariable String projectId,@RequestParam(defaultValue = "0") int page,Model model) {
		model.addAttribute("usersPage", projectMemberService.getAllUserNotOfProject(query,projectId, page));
		System.out.println(projectMemberService.getAllUserNotOfProject(query,projectId, page).getSize());
		model.addAttribute("projectId",projectId);
		model.addAttribute("queryName",query);
		return "managers/pages/members-to-add";
	}
	
    
	@GetMapping("/project-management/{projectId}/add-member")
	public String addMembertoProject(String query,@PathVariable String projectId,String userId) {
		int currentPage = projectMemberService.addMember(userId, projectId);
		return "redirect:/manager/project-management/"+projectId+"/addMembers?query="+query+"&page="+currentPage;
	}
	
	@GetMapping("/project-management/{projectId}/delete-member")
	public String deleteMemberfromProject(String query,@PathVariable String projectId,String userId) {
		int currentPage = projectMemberService.deleteMember(userId,projectId);
		return "redirect:/manager/project-management/"+projectId+"/members?query="+query+"&page="+currentPage;
	}

}
