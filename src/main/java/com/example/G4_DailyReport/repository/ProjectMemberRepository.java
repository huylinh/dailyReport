package com.example.G4_DailyReport.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.G4_DailyReport.model.ProjectMember;
@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, UUID>{
    List<ProjectMember> findByUserId(UUID userId);

}
