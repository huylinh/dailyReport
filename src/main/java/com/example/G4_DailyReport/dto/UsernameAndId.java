package com.example.G4_DailyReport.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode
public class UsernameAndId {
    UUID id;
    String username;

    public UsernameAndId(UUID id, String username) {
        this.id = id;
        this.username = username;
    }
}
