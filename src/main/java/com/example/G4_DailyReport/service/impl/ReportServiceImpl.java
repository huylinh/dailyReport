package com.example.G4_DailyReport.service.impl;

import com.example.G4_DailyReport.bean.ReportBean;
import com.example.G4_DailyReport.controller.manager.request.ReportFilter;
import com.example.G4_DailyReport.exception.IdNotFoundException;
import com.example.G4_DailyReport.model.User;
import com.example.G4_DailyReport.repository.UserRepository;
import com.example.G4_DailyReport.service.ReportService;
import com.example.G4_DailyReport.model.Report;
import com.example.G4_DailyReport.repository.ReportRepository;
import com.example.G4_DailyReport.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Report> retrieveReports() {
        return reportRepository.findAll();
    }


    @Override
    public Page<ReportBean> findPaginated(Pageable pageable, ReportFilter reportFilter) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<ReportBean> list;


        //TODO: Get received reports of current user
        List<Report> receivedReports = retrieveReceivedReports();

        //TODO: add createdByUser
        List<ReportBean> reportBeans = receivedReports.stream().map(report -> {
                    ReportBean reportBean = modelMapper.map(report, ReportBean.class);
                    User user = userRepository.findByUserName(report.getCreatedBy()).orElse(null);
                    reportBean.setCreatedByUser(user);
                    return reportBean;
                }
        ).toList();


        //TODO: filter by name and date
        List<ReportBean> filteredReports = filterNameAndDate(reportBeans, reportFilter.getName(), reportFilter.getDate());

        if (filteredReports.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, filteredReports.size());
            list = filteredReports.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), filteredReports.size());
    }

    @Override
    public ReportBean findReportById(UUID id) {
        Report report = reportRepository.findById(id).orElseThrow(()-> new IdNotFoundException("Not found report with id: " + id));
        ReportBean reportBean = modelMapper.map(report,ReportBean.class);
        User user = userRepository.findByUserName(report.getCreatedBy()).orElseThrow(() -> new UsernameNotFoundException("Username not found "+ report.getCreatedBy()));
        reportBean.setCreatedByUser(user);
        return reportBean;
    }

    private List<ReportBean> filterNameAndDate(List<ReportBean> receivedReports, String name, LocalDate date) {

        return receivedReports.stream()
                .filter(report ->
                        report.getReportDate().isEqual(date) &&
                        report.getCreatedByUser().getName().contains(name)
                        ).collect(Collectors.toList());
    }

    private List<Report> retrieveReceivedReports() {
        User user = userRepository.findByUserName(CurrentUserUtil.getCurrentUser().getUsername()).orElse(null);
        return user.getReports();
    }

}
