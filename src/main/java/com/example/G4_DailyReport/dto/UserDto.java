package com.example.G4_DailyReport.dto;

import com.example.G4_DailyReport.validation.PasswordMatches;
import com.example.G4_DailyReport.validation.ValidEmail;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String password;
    private String matchingPassword;

    @NotNull
    @NotEmpty
    private String email;
}
