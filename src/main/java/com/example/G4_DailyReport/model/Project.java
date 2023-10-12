package com.example.G4_DailyReport.model;

import com.example.G4_DailyReport.enums.ProjectStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@Table(name = "projects")
public class Project extends BaseEntity {
    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonIgnore
    private List<Report> reports;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true
//            ,fetch = FetchType.EAGER
    )
    @JsonIgnore
    private List<ProjectProcess> projectProcesses;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ProjectMember> projectMembers;
}
