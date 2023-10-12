package com.example.G4_DailyReport.validation;

import com.example.G4_DailyReport.dto.ImportUserDto;
import com.example.G4_DailyReport.dto.UsernameAndId;
import com.example.G4_DailyReport.model.User;
import com.example.G4_DailyReport.repository.DepartmentRepository;
import com.example.G4_DailyReport.repository.PositionRepository;
import com.example.G4_DailyReport.repository.UserRepository;
import com.example.G4_DailyReport.service.UserService;
import groovy.lang.Tuple;
import jakarta.persistence.TupleElement;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class UserImportExcelValidator extends ImportExcelValidator<ImportUserDto, User> {
    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final UserService userService;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final PasswordEncoder passwordEncoder;

    private Map<String, UUID> mapUserIdsByUsername;


    public UserImportExcelValidator(MultipartFile file, int headerStart, ModelMapper mapper,
                                    UserRepository userRepository, UserService userService,
                                    DepartmentRepository departmentRepository, PositionRepository positionRepository,
                                    PasswordEncoder passwordEncoder
    ) {
        super(file, headerStart, LastCellNum.BLOCK_UNIT.value());
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.userService = userService;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public ImportExcelValidator<ImportUserDto, User> validate() {
        // Convert data thành list
        List<ImportUserDto> importUserDtos = this.getSourceObjects();

        // Lấy dữ liệu ra để check
        Set<String> usernames = importUserDtos.stream().map(ImportUserDto::getUserName).collect(Collectors.toSet());

        // Thực hiên query 1 loạt, sắm trước để tý chỉ việc dùng để check trong for. Tuyệt đối không được viêt query trong for
        mapUserIdsByUsername = new HashMap<>();
        List<UsernameAndId> lst = userRepository.findAllUserIdByUserName(usernames);
        for (UsernameAndId usernameAndId: lst) {
            mapUserIdsByUsername.put(usernameAndId.getUsername(), usernameAndId.getId());
        }

        int temp = 0;

        // thực hiện for validate lỗi ở đây, validate ở đây thường là validate logic giữa nhiều bảng
        for (ImportUserDto dto: this.getSourceObjects()) {
            List<String> keyComments = new ArrayList<>();
            UUID userId = mapUserIdsByUsername.get(dto.getUserName());

            if (userId != null) {
                keyComments.add("User " + dto.getUserName() + "đã tồn tại");
            }

            // xac dinh từng dòng có lỗi và vị trí dòng lỗi
            temp++;
            this.errorMessengers(keyComments, temp);
        }

        return this;
    }

    @Override
    public List<User> thenMappingAndReturnDestinationObjects() {
        // mapping sourceObjects to destinationObjects;
        List<User> users = new ArrayList<>();

        for (ImportUserDto dto: this.getSourceObjects()) {
            User user = new User();

            // ... mapping
            mapper.map(dto, user);
            user.setDateOfBirth(LocalDate.parse(dto.getDateOfBirth()));
            user.setPassword(passwordEncoder.encode(dto.getPassword()));

            // ... relationship

            users.add(user);
        }

        return users;
    }

}


enum LastCellNum {

    METER(6),
    MATERIAL(10),
    BLOCK_UNIT(6),
    FACILITY(13),
    HEADCOUNT_CONFIG(6),
    BANK_TRANSFER_TRANSACTION(7),
    PARKING(12),
    KEY_CARD(12),
    METER_READING(4),
    RESIDENT(18),
    PAYMENT_TRANSACTION(9),
    FEE_INFO(6),
    VIRTUAL_BANK_ACCOUNT(3);

    LastCellNum(int value) {
        this.value = value;
    }

    final int value;

    public int value() {
        return value;
    }

}