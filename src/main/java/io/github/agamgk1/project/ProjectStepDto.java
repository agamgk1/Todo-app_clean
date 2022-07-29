package io.github.agamgk1.project;

import javax.validation.constraints.NotBlank;

public class ProjectStepDto {

    private int id;
    @NotBlank(message = "Project step's description must ot be empty")
    private String description;
    private int daysToDeadline;


    public ProjectStepDto(int id, String description, int daysToDeadline) {
        this.id = id;
        this.description = description;
        this.daysToDeadline = daysToDeadline;
    }

    public ProjectStepDto() {

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

    public int getDaysToDeadline() {
        return daysToDeadline;
    }

    public void setDaysToDeadline(int daysToDeadline) {
        this.daysToDeadline = daysToDeadline;
    }
    ProjectStep toProjectStep(Project project) {
        var result = new ProjectStep();
        result.setId(id);
        result.setDescription(description);
        result.setDaysToDeadline(daysToDeadline);
        result.setProject(project);
        return result;
    }
}
