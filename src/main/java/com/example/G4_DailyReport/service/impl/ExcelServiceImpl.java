package com.example.G4_DailyReport.service.impl;

import com.example.G4_DailyReport.dto.ImportUserDto;
import com.example.G4_DailyReport.exception.ImportEmailFailedException;
import com.example.G4_DailyReport.model.User;
import com.example.G4_DailyReport.repository.DepartmentRepository;
import com.example.G4_DailyReport.repository.PositionRepository;
import com.example.G4_DailyReport.repository.UserRepository;
import com.example.G4_DailyReport.service.ExcelService;
import com.example.G4_DailyReport.service.UserService;
import com.example.G4_DailyReport.util.Constants;
import com.example.G4_DailyReport.validation.ImportExcelValidator;
import com.example.G4_DailyReport.validation.UserImportExcelValidator;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ExcelServiceImpl implements ExcelService {
    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final UserService userService;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final PasswordEncoder passwordEncoder;

    public int createFromExcel(MultipartFile file, HttpServletResponse response) throws ImportEmailFailedException {
        ImportExcelValidator<ImportUserDto, User> userImportValidator = new UserImportExcelValidator(
                file, Constants.USER_HEADER_START, mapper,
                userRepository, userService, departmentRepository, positionRepository,
                passwordEncoder);
        userImportValidator.validatePre().thenReturnExcelIfError(response);
        List<User> users = userImportValidator.validate().thenReturnExcelIfError(response).thenMappingAndReturnDestinationObjects();

        // Save data
        users = userRepository.saveAll(users);
        return users.size();
    }
}
