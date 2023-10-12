package com.example.G4_DailyReport.dto;

import com.poiji.annotation.ExcelCell;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
//@ExcelCheckDuplication
public class ImportUserDto {
    @ExcelCell(0)
    @NotBlank(message = "username_empty")
//    @Size(max = 100, message = "name_validation_size")
    private String userName;

    @ExcelCell(1)
    @NotBlank(message = "password_empty")
//    @Size(max = 100, message = "name_validation_size")
    private String password; // not encrypted

    @ExcelCell(2)
    @NotBlank(message = "name_empty")
//    @Size(max = 100, message = "name_validation_size")
    private String name;

    @ExcelCell(3)
    @NotBlank(message = "roles_empty")
//    @Size(max = 100, message = "name_validation_size")
    private String role;

    @ExcelCell(4)
//    @Size(max = 512, message = "name_validation_size")
    private String description;

    @ExcelCell(5)
//    @Size(max = 512, message = "name_validation_size")
    private String avatar;

    @ExcelCell(6)
    private String dateOfBirth;

    // relationships
//    @ExcelCell(7)
//    @NotBlank(message = "department_empty")
//    private String departmentName;
//
//    @ExcelCell(8)
//    @NotBlank(message = "position_empty")
//    private String positionName;
}
