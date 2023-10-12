package com.example.G4_DailyReport.spring;

import com.example.G4_DailyReport.model.User;
import com.example.G4_DailyReport.model.UserSecurity;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static java.util.Optional.of;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken) && authentication != null){
            UserSecurity user = (UserSecurity) authentication.getPrincipal();
            return of(user.getUsername());
        }
       return Optional.of("trongnghia2");
    }
}
