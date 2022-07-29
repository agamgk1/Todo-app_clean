package io.github.agamgk1.project;

import io.github.agamgk1.project.Project;
import io.github.agamgk1.project.ProjectStep;
import io.github.agamgk1.project.ProjectStepDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ProjectDtoReadModel {

    private int id;
    @NotBlank(message = "Project's description must not be empty")
    private String description;
    @Valid
    private Set<ProjectStepDto> steps = new HashSet<>();

    public ProjectDtoReadModel(int id, String description, Set<ProjectStepDto> steps) {
        this.id = id;
        this.description = description;
        this.steps = steps;
    }

    public ProjectDtoReadModel() {
    }

    public ProjectDtoReadModel(Project source) {
        this.id = source.getId();
        this.description = source.getDescription();
        this.steps = source.getSteps().stream()
                .map(ProjectStep::toProjectStepDto)
                .collect(Collectors.toSet());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ProjectStepDto> getSteps() {
        return steps;
    }

    public void setSteps(Set<ProjectStepDto> steps) {
        this.steps = steps;
    }
}
