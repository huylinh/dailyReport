package com.example.G4_DailyReport.repository;

import com.example.G4_DailyReport.model.MemberJobsProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNullApi;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberJobsProgressRepository extends JpaRepository<MemberJobsProgress, UUID> {
    Optional<MemberJobsProgress> findById(UUID id);
    List<MemberJobsProgress> findByUserId(UUID userId);
}
