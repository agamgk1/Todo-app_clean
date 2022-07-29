package io.github.agamgk1.task;

public class TaskDtoGroupReadModel {
    private String description;
    private boolean done;

    public TaskDtoGroupReadModel(Task source) {
        description = source.getDescription();
        done = source.isDone();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
