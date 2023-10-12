package com.example.G4_DailyReport.service;

import com.example.G4_DailyReport.bean.ReportBean;
import com.example.G4_DailyReport.controller.manager.request.ReportFilter;
import com.example.G4_DailyReport.model.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;


public interface ReportService {
    List<Report> retrieveReports();
    Page<ReportBean> findPaginated(Pageable pageable, ReportFilter reportFilter);

    ReportBean findReportById(UUID id);
}
