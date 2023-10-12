package com.example.G4_DailyReport.controller.manager;

import com.example.G4_DailyReport.bean.ReportBean;
import com.example.G4_DailyReport.controller.manager.request.ReportFilter;
import com.example.G4_DailyReport.exception.IdNotFoundException;
import com.example.G4_DailyReport.model.Report;
import com.example.G4_DailyReport.repository.DepartmentRepository;
import com.example.G4_DailyReport.repository.ReportRepository;
import com.example.G4_DailyReport.repository.UserRepository;
import com.example.G4_DailyReport.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller("ManagerReportController")
@RequiredArgsConstructor
@RequestMapping("/manager/reports")
public class ReportController {

    private final ReportService reportService;


    @GetMapping("/{id}")
    public ResponseEntity<ReportBean> retrieveReport(@PathVariable UUID id, Model model) {
        ReportBean reportBean = reportService.findReportById(id);
        return new ResponseEntity<>(reportBean, HttpStatus.OK);
    }

    @GetMapping("")
    public String retrieveReports(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam("name") Optional<String> name,
            @RequestParam("date") Optional<LocalDate> date
    ) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        ReportFilter reportFilter = new ReportFilter();
        reportFilter.setDate(date.orElse(LocalDate.now()));
        reportFilter.setName(name.orElse(""));

        Page<ReportBean> reportPage = reportService.findPaginated(PageRequest.of(currentPage - 1, pageSize), reportFilter);

        model.addAttribute("reportPage", reportPage);

        int totalPages = reportPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers;
            if (totalPages < 11) {
                pageNumbers = IntStream.rangeClosed(2, totalPages - 1).boxed()
                        .collect(Collectors.toList());
            } else if (reportPage.getNumber() < 5) {
//                < 1 2 3 4 5 6 .... 12>
                pageNumbers = IntStream.rangeClosed(2, 6).boxed()
                        .collect(Collectors.toList());
            } else if (reportPage.getNumber() > totalPages - 5) {
//                < 1 .... 8 9 10 11 12 >
                pageNumbers = IntStream.rangeClosed(totalPages - 5, totalPages - 1).boxed()
                        .collect(Collectors.toList());
            } else {
//                < 1 ... 10 11 12 13 .. 20 >
                pageNumbers = IntStream.rangeClosed(reportPage.getNumber() - 1, reportPage.getNumber() + 3).boxed()
                        .collect(Collectors.toList());
            }
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("date", date.orElse(LocalDate.now()));
        model.addAttribute("name", name.orElse(""));
        model.addAttribute("activeTab", "reports");

        return "managers/pages/reports";
    }
}
