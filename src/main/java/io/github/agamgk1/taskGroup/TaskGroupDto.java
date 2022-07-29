package io.github.agamgk1.taskGroup;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.agamgk1.project.ProjectDtoReadModel;
import io.github.agamgk1.task.TaskDto;

import java.util.List;

@JsonDeserialize(as = TaskGroupDto.DeserializationImpl.class)
public interface TaskGroupDto {
    static TaskGroupDto create(int id, String description, boolean done, List<TaskDto> tasks, ProjectDtoReadModel project) {
        return new DeserializationImpl(id, description, done, tasks, project);
    }
    int getId();
    String getDescription();
    boolean isDone();
    List<TaskDto> getTasks();
    ProjectDtoReadModel getProject();

    class DeserializationImpl implements TaskGroupDto {
        private final int id;
        private final String description;
        private final boolean done;
        private final List<TaskDto> tasks;
        private final ProjectDtoReadModel project;

        public DeserializationImpl(int id, String description, boolean done, List<TaskDto> tasks, ProjectDtoReadModel project) {
            this.id = id;
            this.description = description;
            this.done = done;
            this.tasks = tasks;
            this.project = project;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public boolean isDone() {
            return done;
        }

        @Override
        public List<TaskDto> getTasks() {
            return tasks;
        }

        @Override
        public ProjectDtoReadModel getProject() {
            return project;
        }

    }
}
