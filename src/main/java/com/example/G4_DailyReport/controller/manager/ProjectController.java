package com.example.G4_DailyReport.controller.manager;

import com.example.G4_DailyReport.enums.ProjectStatus;
import com.example.G4_DailyReport.model.Project;
import com.example.G4_DailyReport.service.ProjectService;
import com.example.G4_DailyReport.model.ProjectProcess;
import com.example.G4_DailyReport.service.ProjectProcessService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectProcessService projectProcessService;

    @PostMapping("")
    public String create(@ModelAttribute("project") Project project, RedirectAttributes redirectAttributes,HttpServletRequest request){
        projectService.create(project);
        redirectAttributes.addFlashAttribute("message", "Data processed successfully!");
        return "redirect:" + request.getRequestURI();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> show(@PathVariable UUID id) {
        Project project = projectService.findById(id);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> update(@PathVariable UUID id, @RequestBody Project project) {
        Project updatedProject = projectService.update(id,project);
        return new ResponseEntity<>(updatedProject,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id){
        projectService.delete(id);
        return new ResponseEntity<>("Delete successfully!",HttpStatus.OK);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("")
    public String index(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam("name") Optional<String> name
    ) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        String nameProject = name.orElse("");

        Page<Project> projectPage = projectService.findPaginated(PageRequest.of(currentPage - 1, pageSize), nameProject);
        // TODO: Phân trang chi project -> hiển thị thông tin project với các tiến trình.
        model.addAttribute("page", projectPage);

        int totalPages = projectPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = generatePageNumbers(totalPages,projectPage);
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("name", name.orElse(""));

        //Active link sidbebar
        model.addAttribute("activeLink", "projects");
        //handler form
        model.addAttribute("project", new Project());
        model.addAttribute("statuses", ProjectStatus.values());
        return "managers/pages/projects";
    }

    private List<Integer> generatePageNumbers(int totalPages, Page<Project> projectPage){
        List<Integer> pageNumbers;
        if (totalPages < 11) {
            pageNumbers = IntStream.rangeClosed(2, totalPages - 1).boxed()
                    .collect(Collectors.toList());
        } else if (projectPage.getNumber() < 5) {
//                < 1 2 3 4 5 6 .... 12>
            pageNumbers = IntStream.rangeClosed(2, 6).boxed()
                    .collect(Collectors.toList());
        } else if (projectPage.getNumber() > totalPages - 5) {
//                < 1 .... 8 9 10 11 12 >
            pageNumbers = IntStream.rangeClosed(totalPages - 5, totalPages - 1).boxed()
                    .collect(Collectors.toList());
        } else {
//                < 1 ... 10 11 12 13 .. 20 >
            pageNumbers = IntStream.rangeClosed(projectPage.getNumber() - 1, projectPage.getNumber() + 3).boxed()
                    .collect(Collectors.toList());
        }
        return pageNumbers;
    }

    @GetMapping("/{id}/project-processes")
    public String projectProcessesIndex(@PathVariable UUID id,Model model){
        List<ProjectProcess> projectProcesses = projectService.getProjectProcesses(id);
        projectProcesses.sort(Comparator.comparing(ProjectProcess::getStartDate));
        model.addAttribute("projectProcesses",projectProcesses);
        model.addAttribute("projectId",id);
        return "managers/pages/processes";
    }

    @PostMapping("/{id}/project-processes")
    public ResponseEntity<String> createProjectProcesses(@PathVariable UUID id,@RequestBody ProjectProcess projectProcess){
        Project existingProject = projectService.findById(id);
        projectProcess.setProject(existingProject);
        ProjectProcess createdProjectProcess = projectProcessService.save(projectProcess);
//        return new ResponseEntity<>(createdProjectProcess, HttpStatus.CREATED);
        return new ResponseEntity<>("Created", HttpStatus.CREATED);
    }

}
