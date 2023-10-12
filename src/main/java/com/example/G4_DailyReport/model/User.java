package com.example.G4_DailyReport.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.G4_DailyReport.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.time.LocalDate;
import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity{

    @NaturalId
    @Column(unique = true, nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    private String name;
//    private String roles;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "text")
    private String avatar;

    private LocalDate dateOfBirth;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="department_id"
            , nullable=true
    )
    private Department department;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "position_id"
//            , nullable=false
    )
    private Position position;

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Report> reports;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<ProjectMember> projectMembers;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<MemberJobsProgress> memberJobsProgresses;
    //--- Constructor --------------------------------
    public User(String username, String password, Role role) {
        this.userName = username;
        this.password = password;
        this.role = role;
    }

}