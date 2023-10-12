package com.example.G4_DailyReport.dto;

import com.example.G4_DailyReport.model.MemberJobsProgress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberProgressDTO {
    private List<MemberJobsProgress> member_jobs = new ArrayList<>();
}
