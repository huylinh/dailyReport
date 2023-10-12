package com.example.G4_DailyReport.service;

import com.example.G4_DailyReport.model.ProjectJob;
import com.example.G4_DailyReport.model.ProjectProcess;

import java.util.List;
import java.util.UUID;

public interface ProjectProcessService {

    ProjectProcess save(ProjectProcess projectProcess);

    ProjectProcess findById(UUID id);

    ProjectProcess update(UUID id, ProjectProcess project);

    void delete(UUID id);

    List<ProjectJob> getProjectJobs(UUID id);

    ProjectJob createJob(UUID id, ProjectJob projectJob);
}
