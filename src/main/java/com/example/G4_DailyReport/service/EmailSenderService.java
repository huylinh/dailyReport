package com.example.G4_DailyReport.service;

import com.example.G4_DailyReport.enums.Role;
import com.example.G4_DailyReport.model.Project;
import com.example.G4_DailyReport.model.Report;
import com.example.G4_DailyReport.model.User;
import com.example.G4_DailyReport.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserRepository userRepo;

    private void sendReportNotifyEmailToManager(
            String managerEmail,
            String managerName,
            String reporterName,
            String reportUrl
    ) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(managerEmail);
        mailMessage.setSubject("New report from " + reporterName);
        mailMessage.setText(
                "Hi, " + managerName + "\n" +
                "\n" +
                "You have a new report from your " + reporterName + ".\n" +
                "Please check it out at: " + reportUrl +
                "\n" +
                "\n" +
                "© 2023 G4DailyReport, Inc. All rights reserved."
        );

        mailSender.send(mailMessage);
    }

    public void sendReportNotifyEmailToManager(Report report) {
        Project project = report.getProject();
        List<User> managers = userRepo.findAllMemberInProjectByProjectIdAndRole(project.getId(), Role.ROLE_MANAGER);
        for (User manager : managers) {
            this.sendReportNotifyEmailToManager(
                    manager.getUserName(),  // note: username is email
                    manager.getName(),
                    report.getUser().getName(),
                    "https://************/" + report.getId()
            );
        }
    }


//    You can copy this method to G4DailyReportApplication class to test
//    When you run the application, this method will be called
//    And you will receive an email
//
//    @Autowired
//    EmailSenderService emailSenderService;
//    @EventListener(ApplicationReadyEvent.class)
//    public void triggerEmail() {
//        emailSenderService.sendReportNotifyEmailToManager(
//                "vuthanhha.2001@gmail.com",
//                "Test",
//                "hahunavth@gmail.com",
//                "Test2",
//                    "https://www.google.com/"
//        );
//    }
}
