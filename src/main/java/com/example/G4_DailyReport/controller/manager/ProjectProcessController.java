package com.example.G4_DailyReport.controller.manager;

import com.example.G4_DailyReport.model.Project;
import com.example.G4_DailyReport.model.ProjectJob;
import com.example.G4_DailyReport.model.ProjectProcess;
import com.example.G4_DailyReport.service.ProjectProcessService;
import groovy.lang.GString;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/manager/project-processes")
@RequiredArgsConstructor
public class ProjectProcessController {
    private final ProjectProcessService projectProcessService;

    @GetMapping("/{id}")
    public ResponseEntity<ProjectProcess> show(@PathVariable UUID id) {
        ProjectProcess projectProcess = projectProcessService.findById(id);
        return new ResponseEntity<>(projectProcess, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectProcess> update(@PathVariable UUID id, @RequestBody ProjectProcess project) {
        ProjectProcess updatedProjectProcess = projectProcessService.update(id, project);
        return new ResponseEntity<>(updatedProjectProcess, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        projectProcessService.delete(id);
        return new ResponseEntity<>("Delete process complete", HttpStatus.OK);
    }

    @GetMapping("/{id}/project-jobs")
    public ResponseEntity<List<ProjectJob>> projectJobsIndex(@PathVariable UUID id) {
        List<ProjectJob> projectJobs = projectProcessService.getProjectJobs(id);
        projectJobs.sort((r1, r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt()));
        return new ResponseEntity<>(projectJobs, HttpStatus.OK);
    }
    @PostMapping("/{id}/project-jobs")
    public ResponseEntity<ProjectJob> projectJobsCreate(@PathVariable UUID id, @RequestBody ProjectJob projectJob) {
        ProjectJob createdJob = projectProcessService.createJob(id, projectJob);
        return new ResponseEntity<>(createdJob, HttpStatus.CREATED);
    }
}
