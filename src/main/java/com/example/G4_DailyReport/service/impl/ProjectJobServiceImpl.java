package com.example.G4_DailyReport.service.impl;

import com.example.G4_DailyReport.exception.IdNotFoundException;
import com.example.G4_DailyReport.model.ProjectJob;
import com.example.G4_DailyReport.repository.ProjectJobRepository;
import com.example.G4_DailyReport.service.ProjectJobService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectJobServiceImpl implements ProjectJobService {
    private final ProjectJobRepository projectJobRepository;
    private final ModelMapper modelMapper;


    @Override
    public ProjectJob update(UUID id, ProjectJob projectJob) {
        ProjectJob existingProjectJob =  projectJobRepository.findById(id).orElseThrow(() -> new IdNotFoundException("Project job not found"));
        modelMapper.map(projectJob,existingProjectJob);
        projectJobRepository.save(existingProjectJob);
        return existingProjectJob;
    }

    @Override
    public void deleteById(UUID id) {
        projectJobRepository.deleteById(id);
    }

    @Override
    public ProjectJob findById(UUID id) {
        return projectJobRepository.findById(id).orElseThrow(()-> new IdNotFoundException("Project job not found~"));
    }
}
