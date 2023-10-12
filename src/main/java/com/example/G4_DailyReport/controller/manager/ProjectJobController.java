package com.example.G4_DailyReport.controller.manager;

import com.example.G4_DailyReport.model.Project;
import com.example.G4_DailyReport.model.ProjectJob;
import com.example.G4_DailyReport.service.ProjectJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/manager/project-jobs")
@RequiredArgsConstructor
public class ProjectJobController {
    private final ProjectJobService projectJobService;


    @GetMapping("/{id}")
    public ResponseEntity<ProjectJob> show(@PathVariable UUID id) {
        ProjectJob projectJob = projectJobService.findById(id);
        return new ResponseEntity<>(projectJob, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        projectJobService.deleteById(id);
        return new ResponseEntity<>("Delete process complete", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectJob> update(@PathVariable UUID id, @RequestBody ProjectJob projectJob) {
        ProjectJob updatedProjectJob = projectJobService.update(id,projectJob);
        return new ResponseEntity<>(updatedProjectJob,HttpStatus.OK);
    }

}
