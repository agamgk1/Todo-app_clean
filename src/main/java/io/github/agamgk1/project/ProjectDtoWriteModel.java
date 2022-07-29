package io.github.agamgk1.project;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectDtoWriteModel {
    private String description;
    @Valid
    private List<ProjectStepDto> steps = new ArrayList<>();

    public ProjectDtoWriteModel() {
        steps.add(new ProjectStepDto());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ProjectStepDto> getSteps() {
        return steps;
    }

    public void setSteps(List<ProjectStepDto> steps) {
        this.steps = steps;
    }

    public Project toProject() {
        var result = new Project();
        result.setDescription(description);
        result.setSteps(steps.stream()
                .map(projectStepDto -> projectStepDto.toProjectStep(result))
                .collect(Collectors.toSet()));
        return result;
    }
}
