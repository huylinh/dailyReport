package com.example.G4_DailyReport.service;

import com.example.G4_DailyReport.model.Project;

import java.util.UUID;

import com.example.G4_DailyReport.dto.MemberProgressDTO;
import com.example.G4_DailyReport.enums.JobStatus;
import com.example.G4_DailyReport.model.*;
import com.example.G4_DailyReport.repository.*;
import com.example.G4_DailyReport.util.CurrentUserUtil;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserProjectService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ProjectMemberRepository projectMemberRepository;
    @Autowired
    ProjectProcessRepository projectProcessRepository;
    @Autowired
    ProjectJobsRepository projectJobsRepository;
    @Autowired
    MemberJobsProgressRepository memberJobsProgressRepository;
    List<Project> assignedProject;
    List<ProjectJob> assignedProjectJob;
    @Getter
    List<MemberJobsProgress> memberJobsProgressList;
    UUID currentUserId;

    @PostConstruct
    public void init() {
        this.assignedProject = new ArrayList<>();
        this.assignedProjectJob = new ArrayList<>();
        this.memberJobsProgressList = new ArrayList<>();
    }

    public List<Project> getAssignedProject() {
        this.currentUserId = userRepository.findByUserName(CurrentUserUtil.getCurrentUser().getUsername()).get().getId();
        List<ProjectMember> projectIdList = projectMemberRepository.findByUserId(currentUserId);
        projectIdList.forEach(value -> {
            assignedProject.add(projectRepository.findProjectById(value.getProject().getId()));
        });
        return assignedProject;
    }

    public List<ProjectProcess> getAllProcesses(UUID projectId) {
        return projectProcessRepository.findByProjectIdOrderByStartDate(projectId);
    }

    public List<MemberJobsProgress> getAllAssignedJobs(UUID processId) {
        this.memberJobsProgressList.clear();
        // Finding all jobs assigned
        UUID currentUserId = userRepository.findByUserName(CurrentUserUtil.getCurrentUser().getUsername()).get().getId();
        List<MemberJobsProgress> allAssignedJobs = memberJobsProgressRepository.findByUserId(currentUserId);
        // Finding all assigned jobs in this particular process
        List<ProjectJob> allJobs = projectJobsRepository.findByProjectProcessIdOrderByCreatedAt(processId);
        allAssignedJobs.forEach(
                value -> {
                    if (allJobs.contains(value.getProjectJob())) {
                        this.memberJobsProgressList.add(value);
                    }
                }
        );
        return this.memberJobsProgressList;
    }

    public void updateJobStatus(UUID processId, List<MemberJobsProgress> memberJobsProgressList) {
        //Set status
        memberJobsProgressList.forEach(value -> {
            value.setUpdatedAt(LocalDateTime.now());
            if (value.getUpdatedAt().isAfter(projectProcessRepository.findById(processId).get().getEndDate().atStartOfDay())) {
                if (value.isTempStatus()) {
                    value.setStatus(JobStatus.LATE_SUBMISSION);
                } else {
                    value.setStatus(JobStatus.IN_PROGRESS);
                }
            } else {
                if (value.isTempStatus()) {
                    value.setStatus(JobStatus.COMPLETED);
                }
            }
            memberJobsProgressRepository.save(value);
        });
        this.memberJobsProgressList = memberJobsProgressRepository.findAll();
    }
}
