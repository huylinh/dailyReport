package com.example.G4_DailyReport.repository;

import com.example.G4_DailyReport.model.Project;
import com.example.G4_DailyReport.model.ProjectMember;
import com.example.G4_DailyReport.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    Project findProjectById(UUID uuid);
    Optional<List<Project>> findByCreatedBy(String username);
    @Query("SELECT p FROM Project p WHERE p.createdBy = :username ORDER BY p.createdAt DESC")
    Optional<List<Project>> findByIdSorted(String username);
    List<ProjectMember> findProjectMemberById(UUID uuid);
}
