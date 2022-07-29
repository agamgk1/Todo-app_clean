package io.github.agamgk1.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tasks")
class TaskController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);
    private final ApplicationEventPublisher eventPublisher;
    private final TaskRepository taskRepository;
    private final TaskQueryRepository taskQueryRepository;

    public TaskController(ApplicationEventPublisher eventPublisher, TaskRepository taskRepository, TaskQueryRepository taskQueryRepository) {
        this.eventPublisher = eventPublisher;
        this.taskRepository = taskRepository;
        this.taskQueryRepository = taskQueryRepository;
    }
    @GetMapping(params = {"!sort","!page","!size"})
    ResponseEntity<List<Task>> readAllTasks() {
        LOGGER.warn("Exposing all tasks");
        return ResponseEntity.ok(taskQueryRepository.findAll());
    }
    @GetMapping()
    ResponseEntity<List<Task>> readAllTasks(Pageable pageable) {
        LOGGER.info("Custom pager");
        return ResponseEntity.ok(taskQueryRepository.findAll(pageable).getContent());
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<Task> readTask(@PathVariable int id) {
        return taskRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/done")
    ResponseEntity<List<Task>> readDoneTasks(@RequestParam(defaultValue = "true") boolean state) {
        return ResponseEntity.ok(taskQueryRepository.findByDone(state));
    }

    @PostMapping()
    ResponseEntity<Task> createTask(@RequestBody @Valid Task toCreate) {
       Task result = taskRepository.save(toCreate);
        return ResponseEntity.created(URI.create("/"+result.getId())).body(result);
    }

    @PutMapping("/{id}")
    ResponseEntity<Task> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate) {
        if (!taskQueryRepository.existsById(id)) {
            return ResponseEntity.notFound().build(); //404
        }
        taskRepository.findById(id)
                .ifPresent(task -> { task.updateFrom(toUpdate);
        taskRepository.save(task);
    });
        return ResponseEntity.noContent().build(); //202
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<Task> toggleTask(@PathVariable int id) {
        if(!taskQueryRepository.existsById(id)) {
            return ResponseEntity.notFound().build(); //404
        }
        taskRepository.findById(id)
                .map(Task::toggle)
                .ifPresent(eventPublisher::publishEvent);
        return ResponseEntity.noContent().build(); //202
    }

}
