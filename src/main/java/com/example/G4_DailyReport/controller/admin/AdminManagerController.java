package com.example.G4_DailyReport.controller.admin;

import com.example.G4_DailyReport.model.User;
import com.example.G4_DailyReport.service.DepartmentService;
import com.example.G4_DailyReport.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin/departments/{departmentId}/managers")
public class AdminManagerController {
    @Autowired
    private UserService userService;
    @Autowired
    private DepartmentService departmentService;

    private List<UUID> selectedManagerIds = Collections.emptyList();

    @GetMapping("new")
    public String add(
            Model model,
            @PathVariable("departmentId") String departmentId,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "query", defaultValue = "") String query
    ) {
        PageRequest pageRequest = PageRequest.of(
                        page - 1,
                        size,
                        Sort.by(sort).ascending()
                );
        Page<User> managersWithoutDepartment = userService.findAllManagerByDepartmentIsNull(query, pageRequest);

        model.addAttribute("query", query);
        model.addAttribute("pageSize", page);
        model.addAttribute("currentPage", page);
        model.addAttribute("page", managersWithoutDepartment);
        model.addAttribute("departmentId", departmentId);
        return "admin/managers/new";
    }

    @PostMapping("submit")
    public String submitSelectedItems(
            @RequestParam(value = "selectedManagerIds", required = false) List<UUID> selectedManagerIds,
            @PathVariable("departmentId") UUID departmentId
    ) {
        this.selectedManagerIds = selectedManagerIds;
        if(selectedManagerIds != null)
            departmentService.addManagers(departmentId, selectedManagerIds);

        return "redirect:/admin/departments/" + departmentId + "/show";
    }

    @ModelAttribute("selectedManagerIds")
    public List<UUID> getSelectedItems() {
        return selectedManagerIds;
    }

    /**
     * TODO:
     * - Allow cors for this method to be able to use DELETE method
     * - Change _manager_table.html to use DELETE method
     */
    @DeleteMapping("delete/{managerId}")
//    @RequestMapping(path = "delete/{managerId}", method = {RequestMethod.DELETE, RequestMethod.POST})
    public String deleteSelectedItems(
            Model model,
            @PathVariable("departmentId") UUID departmentId,
            @PathVariable("managerId") UUID managerId
    ) {
        departmentService.removeManager(departmentId, managerId);

        return "redirect:/admin/departments/" + departmentId + "/show";
    }
}
