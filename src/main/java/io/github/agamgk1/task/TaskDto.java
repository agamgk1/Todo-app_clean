package io.github.agamgk1.task;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.agamgk1.taskGroup.TaskGroupDto;

import java.time.LocalDateTime;

@JsonDeserialize(as = TaskDto.DeserializationImpl.class)
public interface TaskDto {
    static TaskDto create(int id, String description, boolean done, LocalDateTime deadline, TaskGroupDto group) {
        return new DeserializationImpl(id, description, done, deadline, group);
    }

    int getId();
    String getDescription();
    boolean isDone();
    LocalDateTime getDeadline();
    TaskGroupDto getGroup();

    class DeserializationImpl implements TaskDto {
        private final int id;
        private final String description;
        private final boolean done;
        private final LocalDateTime deadline;
        private final TaskGroupDto group;

        public DeserializationImpl(int id, String description, boolean done, LocalDateTime deadline, TaskGroupDto group) {
            this.id = id;
            this.description = description;
            this.done = done;
            this.deadline = deadline;
            this.group = group;
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
        public LocalDateTime getDeadline() {
            return deadline;
        }

        @Override
        public TaskGroupDto getGroup() {
            return group;
        }
    }
}
