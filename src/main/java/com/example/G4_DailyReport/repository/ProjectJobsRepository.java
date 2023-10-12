package com.example.G4_DailyReport.repository;

import com.example.G4_DailyReport.model.ProjectJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProjectJobsRepository extends JpaRepository<ProjectJob, UUID> {
    List<ProjectJob> findByProjectProcessIdOrderByCreatedAt(UUID projectProcessId);
}
