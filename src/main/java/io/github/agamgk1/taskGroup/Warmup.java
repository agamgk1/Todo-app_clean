package io.github.agamgk1.taskGroup;

import io.github.agamgk1.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
class Warmup implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Warmup.class);

    private final TaskGroupRepository taskGroupRepository;
    private final TaskGroupQueryRepository taskGroupQueryRepository;

    Warmup(TaskGroupRepository taskGroupRepository, TaskGroupQueryRepository taskGroupQueryRepository) {
        this.taskGroupRepository = taskGroupRepository;
        this.taskGroupQueryRepository = taskGroupQueryRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOGGER.info("Application warmup after context refreshed");
        final String description = "ApplicationContextEvent";
        if(!taskGroupQueryRepository.existsByDescription(description)) {
            LOGGER.info("No required group found. Add it");
            var group = new TaskGroup();
            group.setDescription(description);
            group.setTasks(Set.of(
                    new Task("ContextRefreshedEvent", null, group)));
            taskGroupRepository.save(group);
        }
    }
}
