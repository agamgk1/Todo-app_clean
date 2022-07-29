package io.github.agamgk1.taskGroup;

import io.github.agamgk1.project.Project;
import io.github.agamgk1.task.TaskDtoGroupWriteModel;
import io.github.agamgk1.taskGroup.TaskGroup;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskGroupDtoWriteModel {
    @NotBlank(message = "Task group's description must not be empty")
    private String description;
    @Valid
    private List<TaskDtoGroupWriteModel> tasks = new ArrayList<>();

    public TaskGroupDtoWriteModel() {
        tasks.add(new TaskDtoGroupWriteModel());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TaskDtoGroupWriteModel> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDtoGroupWriteModel> tasks) {
        this.tasks = tasks;
    }

    public TaskGroup toGroup(Project project) {
        var result = new TaskGroup();
        result.setDescription(description);
        result.setTasks(
                tasks.stream()
                        .map(source -> source.toTask(result))
                        .collect(Collectors.toSet())
        );
        result.setProject(project);
        return result;
    }
}
