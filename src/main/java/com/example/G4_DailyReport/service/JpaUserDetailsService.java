package com.example.G4_DailyReport.service;

import com.example.G4_DailyReport.model.User;
import com.example.G4_DailyReport.model.UserSecurity;
import com.example.G4_DailyReport.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class JpaUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUserName(userName);

        user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + userName));

        return user.map(UserSecurity::new).get();
//        return userRepository.findByUserName(username).map(UserSecurity::new)
//                .orElseThrow(new UsernameNotFoundException("Username not found" + username));
    }
}
