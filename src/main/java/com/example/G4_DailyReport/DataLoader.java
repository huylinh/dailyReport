package com.example.G4_DailyReport;

import com.example.G4_DailyReport.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner{
    @Autowired
    SecurityService securityService;
    @Override
    public void run(String... args) throws Exception {
        securityService.grantRoles();
    }
}
