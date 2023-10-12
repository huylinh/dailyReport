package com.example.G4_DailyReport.bean;

import com.example.G4_DailyReport.model.*;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class ReportBean {
    private UUID id;

    //additional information
    private User createdByUser;

    private LocalDate reportDate;

    private String tomorrowPlan;

    private String reasonCannotCompleteWork;

    private String actualWork;

    private String workingTime;

    private User user;

    private Project project;

}
