package com.example.G4_DailyReport.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@Table(name = "project_jobs")
public class ProjectJob extends BaseEntity {
    private String content;

    @ManyToOne
    @JoinColumn(name = "project_process_id")
    @JsonIgnore
    private ProjectProcess projectProcess;

    @OneToMany(mappedBy = "projectJob", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<MemberJobsProgress> memberJobsProgresses;
}
