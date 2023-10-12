package com.example.G4_DailyReport.controller.user;

import com.example.G4_DailyReport.dto.MemberProgressDTO;
import com.example.G4_DailyReport.model.MemberJobsProgress;
import com.example.G4_DailyReport.model.Project;
import com.example.G4_DailyReport.service.ProjectService;
import com.example.G4_DailyReport.service.UserProjectService;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.support.SessionStatus;

import java.util.List;
import java.util.UUID;

@Controller("UserProjectController")
@RequiredArgsConstructor
@RequestMapping("/projects")
@SessionAttributes("projects")
public class ProjectController {
    @Autowired
    UserProjectService projectService;

    @ModelAttribute(name = "projects")
    public List<Project> projects() {
        projectService.init();
        return projectService.getAssignedProject();
    }
    @ModelAttribute(name = "member_jobsList")
    public MemberProgressDTO member_jobsList(){
        return new MemberProgressDTO();
    }
    @GetMapping("")
    public String index(){
        return "user/projects/index.html";
    }
    @GetMapping("/{projectId}")
    public String show(Model model, @PathVariable("projectId") UUID projectId){
        model.addAttribute("project_id",projectId);
        model.addAttribute("processes",projectService.getAllProcesses(projectId));
        return "user/projects/show.html";
    }
    @GetMapping("/{projectId}/{processId}/")
    public String showJobs(Model model, @PathVariable("projectId") UUID projectId, @PathVariable("processId") UUID processId){
        projectService.init();
        MemberProgressDTO member_jobsList = member_jobsList();
        member_jobsList.setMember_jobs(projectService.getAllAssignedJobs(processId));
        model.addAttribute("project_id",projectId);
        model.addAttribute("process_id",processId);
        if(member_jobsList.getMember_jobs().isEmpty()){
            return "user/projects/no_job.html";
        }
        model.addAttribute("member_jobsList",member_jobsList);
        return "user/projects/show_jobs.html";
    }
    @PostMapping("/{projectId}/{processId}/")
    public String updateJobs(Model model, @ModelAttribute MemberProgressDTO member_jobsList,
                             @PathVariable("projectId") UUID projectId, @PathVariable("processId") UUID processId){
        projectService.updateJobStatus(processId,member_jobsList.getMember_jobs());
        return "redirect:/projects/"+projectId+"/"+processId+"/";
    }
}
