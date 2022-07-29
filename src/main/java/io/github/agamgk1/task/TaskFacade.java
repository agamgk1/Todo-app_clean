package io.github.agamgk1.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
class TaskFacade {
    private final TaskRepository taskRepository;
    private final TaskQueryRepository taskQueryRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskFacade.class);

    public TaskFacade(TaskRepository taskRepository, TaskQueryRepository taskQueryRepository) {
        this.taskRepository = taskRepository;
        this.taskQueryRepository = taskQueryRepository;
    }
    @Async
    CompletableFuture<List<Task>> findAllAsync() {
        LOGGER.info("supply async");
        return CompletableFuture.supplyAsync(taskQueryRepository::findAll);
    }

    public Optional<Task> findById(Integer id) {
        return taskRepository.findById(id);
    }

    public Task save(Task entity) {
        return taskRepository.save(entity);
    }
}
