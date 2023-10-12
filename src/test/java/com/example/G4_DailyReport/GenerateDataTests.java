package com.example.G4_DailyReport;

import com.example.G4_DailyReport.model.Department;
import com.example.G4_DailyReport.model.Position;
import com.example.G4_DailyReport.model.User;
import com.example.G4_DailyReport.repository.DepartmentRepository;
import com.example.G4_DailyReport.repository.PositionRepository;
import com.example.G4_DailyReport.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
public class GenerateDataTests {
    @Autowired
    UserRepository userRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    PositionRepository positionRepository;

    @Test
//    @Transactional
    public void testGenerateData() {
        // reset database
        userRepository.deleteAll();
        departmentRepository.deleteAll();
        positionRepository.deleteAll();

        loadPosition();
        loadDepartment();
        loadUserData();
    }

    private void loadPosition() {
        if (positionRepository.count() == 0) {
            Position position = new Position();
            position.setName("Admin");
            positionRepository.save(position);

            position = new Position();
            position.setName("Manager");
            positionRepository.save(position);

            position = new Position();
            position.setName("Leader");
            positionRepository.save(position);
        }
    }

    private void loadDepartment() {
        if (departmentRepository.count() == 0) {
            Department department = new Department();
            department.setName("IT");
            department.setDescription("This is IT department");
            departmentRepository.save(department);

            department = new Department();
            department.setName("HR");
            department.setDescription("This is HR department");
            departmentRepository.save(department);

            department = new Department();
            department.setName("Accounting");
            department.setDescription("This is Accounting department");
            departmentRepository.save(department);
        }
    }

    private void loadUserData() {
        if (userRepository.count() == 0) {
            List<Position> positions = positionRepository.findAll();
            List<Department> departments = departmentRepository.findAll();
            User user = new User();
            user.setName("Nguyen Van A");
            user.setUserName("user_1");
            user.setPassword("123456");
            user.setAvatar("https://www.w3schools.com/howto/img_avatar.png");
            user.setDateOfBirth(LocalDate.of(1999, 1, 1));
            user.setDepartment(departments.get(0));
            user.setPosition(positions.get(0));
            userRepository.save(user);

            user = new User();
            user.setName("Nguyen Van B");
            user.setUserName("user_2");
            user.setPassword("123456");
            user.setPosition(positions.get(1));
            user.setDepartment(departments.get(1));
            user.setDateOfBirth(LocalDate.of(1999, 1, 1));
            user.setAvatar("https://www.w3schools.com/howto/img_avatar.png");
            userRepository.save(user);

            user = new User();
            user.setName("Nguyen Van C");
            user.setUserName("user_3");
            user.setPassword("123456");
            user.setAvatar("https://www.w3schools.com/howto/img_avatar.png");
            user.setPosition(positions.get(1));
            user.setDateOfBirth(LocalDate.of(1999, 1, 1));
            userRepository.save(user);

            user = new User();
            user.setName("Nguyen Van D");
            user.setUserName("user_4");
            user.setPassword("123456");
            user.setAvatar("https://www.w3schools.com/howto/img_avatar.png");
            user.setPosition(positions.get(2));
            user.setDateOfBirth(LocalDate.of(1999, 1, 1));
            userRepository.save(user);

            for(int i=5; i < 20; i++) {
                user = new User();
                user.setName("Nguyen Van " + i);
                user.setUserName("user_" + i);
                user.setPassword("123456");
                user.setAvatar("https://www.w3schools.com/howto/img_avatar.png");
                user.setDateOfBirth(LocalDate.of(1999, 1, 1));
                userRepository.save(user);
            }
        }
        System.out.println(userRepository.count());
    }
}
