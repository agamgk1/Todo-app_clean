package io.github.agamgk1.taskGroup;

import io.github.agamgk1.task.Task;
import io.github.agamgk1.task.TaskDtoGroupReadModel;
import io.github.agamgk1.taskGroup.TaskGroup;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class TaskGroupDtoReadModel {
    private int id;
    private String description;
    private LocalDateTime deadline;
    private Set<TaskDtoGroupReadModel> tasks;

    public TaskGroupDtoReadModel(TaskGroup source) {
        id = source.getId();
        description = source.getDescription();
        source.getTasks()
                .stream()
                .map(Task::getDeadline)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .ifPresent(localDateTime -> deadline = localDateTime);

        tasks = source.getTasks()
                .stream()
                .map(TaskDtoGroupReadModel::new)
                .collect(Collectors.toSet());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Set<TaskDtoGroupReadModel> getTasks() {
        return tasks;
    }

    public void setTasks(Set<TaskDtoGroupReadModel> tasks) {
        this.tasks = tasks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
