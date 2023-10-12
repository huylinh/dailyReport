package com.example.G4_DailyReport.repository;

import com.example.G4_DailyReport.model.ProjectJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface ProjectJobRepository extends JpaRepository<ProjectJob, UUID> {
}
