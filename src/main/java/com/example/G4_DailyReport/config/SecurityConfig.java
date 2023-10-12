package com.example.G4_DailyReport.config;

import com.example.G4_DailyReport.helper.MySimpleUrlAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import com.example.G4_DailyReport.service.JpaUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }
    @Bean
    //Hashing password
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    private final JpaUserDetailsService jpaUserDetailsService;

    @SuppressWarnings("is deprecated and marked for removal")
//    @Bean
//    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests().anyRequest().permitAll().and().cors().and().csrf().disable();
//        return http.build();
//    }
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.userDetailsService(jpaUserDetailsService);
        http.authorizeHttpRequests()
                    .requestMatchers("/resources/**","/managers/**","/css/**","/user/**").permitAll()
                    .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                    .requestMatchers("/manager/**").hasAuthority("ROLE_MANAGER")
                    .requestMatchers("/**").hasAuthority("ROLE_USER")
                .and()
                    .headers()
                    .frameOptions()
                    .sameOrigin()
                .and()
                    .formLogin()
                    .loginPage("/login.html")
                    .loginProcessingUrl("/login")
                    .successHandler(authenticationSuccessHandler())
                    .failureUrl("/login-error.html")
                    .permitAll()
                .and()
                    .logout().deleteCookies()
                    .permitAll()
                    .logoutSuccessUrl("/")
                .and().cors().and().csrf().disable();
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new MySimpleUrlAuthenticationSuccessHandler();
    }
}