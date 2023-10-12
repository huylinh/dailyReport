package com.example.G4_DailyReport.controller.manager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.G4_DailyReport.model.User;
import com.example.G4_DailyReport.service.ManagerService;
import com.example.G4_DailyReport.util.CurrentUserUtil;
@Controller
@RequiredArgsConstructor
@RequestMapping("/manager")
public class ManagerController {
	@Autowired

	ManagerService managerService;
//	@Autowired
//	private ManagerSingleton managerSingleton;


	@ModelAttribute("user")
	public User loadCurrentUser() {
		return managerService.getUserByUserUName(CurrentUserUtil.getCurrentUser().getUsername());
	}
    @PreAuthorize("hasRole('ROLE_MANAGER')")
	@GetMapping("/departments")
	public String managerHomepage() {
		
		return "managers/pages/department";
	}
    @PreAuthorize("hasRole('ROLE_MANAGER')")
	@GetMapping("/departments/listEmployee")
	public String managerDepartments(Model model,@RequestParam(defaultValue = "")String query, @RequestParam(defaultValue = "0") int page) {
		model.addAttribute("usersPage",
				managerService.getAllUserInDepartment(query,loadCurrentUser().getDepartment().getId().toString(), page));
		model.addAttribute("queryName",query);
		return "managers/pages/employees-in-department";
	}
    @PreAuthorize("hasRole('ROLE_MANAGER')")
	@GetMapping("/departments/addEmployee")
	public String managerAddEmployee(Model model,@RequestParam(defaultValue = "")String query, @RequestParam(defaultValue = "0") int page) {
		model.addAttribute("usersPage", managerService.getAllUserNoDepartment(query,page));
		System.out.println( managerService.getAllUserNoDepartment(query,page).getTotalPages());
		model.addAttribute("queryName",query);
		return "managers/pages/employees-to-add";
	}

	@GetMapping("/departments/user")
	public String addEmployee(String query,String userId) {
		managerService.getUserById(userId).setDepartment(loadCurrentUser().getDepartment());
		int currentPage = managerService.save(managerService.getUserById(userId));
		return "redirect:/manager/departments/addEmployee?query="+query+"&page=" + currentPage;
	}

	@GetMapping("/departments/delete")
	public String deleteEmployee(String query,String userId) {
		int currentPage = managerService.delete(managerService.getUserById(userId));
		return "redirect:/manager/departments/listEmployee?query="+query+"&page=" + currentPage;
	}
}
