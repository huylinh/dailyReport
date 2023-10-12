package com.example.G4_DailyReport.controller.manager.request;

import lombok.Data;

import java.time.LocalDate;
@Data
public class ReportFilter {
    LocalDate date;
    String name;
}
