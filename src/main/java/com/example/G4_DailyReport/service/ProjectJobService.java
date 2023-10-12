package com.example.G4_DailyReport.service;

import com.example.G4_DailyReport.model.Project;
import com.example.G4_DailyReport.model.ProjectJob;

import java.util.UUID;

public interface ProjectJobService {

    ProjectJob update(UUID id, ProjectJob projectJob);

    void deleteById(UUID id);

    ProjectJob findById(UUID id);
}
