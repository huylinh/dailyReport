package com.example.G4_DailyReport.service;

import com.example.G4_DailyReport.exception.ImportEmailFailedException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ExcelService {
    int createFromExcel(MultipartFile file, HttpServletResponse response) throws ImportEmailFailedException;
}
