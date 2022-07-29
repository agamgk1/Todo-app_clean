package io.github.agamgk1.reports;

import io.github.agamgk1.task.TaskDto;
import io.github.agamgk1.task.TaskQueryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reports")
class TaskReportController {
    private final TaskQueryRepository taskQueryRepository;
    private final  PersistedTaskEventRepository eventRepository;

    TaskReportController(TaskQueryRepository taskQueryRepository, PersistedTaskEventRepository eventRepository) {
        this.taskQueryRepository = taskQueryRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping("/count/{id}")
    ResponseEntity<TaskWithChangesCount> readTaskWithCount(@PathVariable int id) {
        return taskQueryRepository.findDtoById(id)
                .map(taskDto -> new TaskWithChangesCount(taskDto, eventRepository.findByTaskId(id)))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private static class TaskWithChangesCount {
        public String description;
        public boolean done;
        public int changesCount;

        TaskWithChangesCount(final TaskDto task, final List<PersistedTaskEvent> events) {
            description = task.getDescription();
            done = task.isDone();
            changesCount = events.size();
        }
    }
}
