package io.github.agamgk1.taskGroup;

import io.github.agamgk1.task.TaskQueryRepository;
import io.github.agamgk1.project.Project;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskGroupFacade {
    private final TaskGroupRepository taskGroupRepository;
    private final TaskGroupQueryRepository taskGroupQueryRepository;
    private final TaskQueryRepository taskQueryRepository;

    public TaskGroupFacade(@Qualifier("taskGroupRepository") TaskGroupRepository repository, TaskGroupQueryRepository taskGroupQueryRepository, TaskQueryRepository taskQueryRepository) {
        this.taskGroupRepository = repository;
        this.taskGroupQueryRepository = taskGroupQueryRepository;
        this.taskQueryRepository = taskQueryRepository;
    }

    TaskGroupDtoReadModel createGroup(TaskGroupDtoWriteModel source) {
        return createGroup(source,null);
    }

    public TaskGroupDtoReadModel createGroup(TaskGroupDtoWriteModel source, Project project) {
        TaskGroup result = taskGroupRepository.save(source.toGroup(project));
        return new TaskGroupDtoReadModel(result);
    }

    List<TaskGroupDtoReadModel> readAll() {
       return taskGroupQueryRepository.findAll()
                .stream()
                .map(TaskGroupDtoReadModel::new)
                .collect(Collectors.toList());
    }

    void toggleGroup(int groupId) {
       if (taskQueryRepository.existsByDoneIsFalseAndGroup_Id(groupId)) {
           throw new IllegalStateException("Group has undone tasks, done all the tasks first");
       }
       TaskGroup result = taskGroupRepository.findById(groupId)
               .orElseThrow(() -> new IllegalArgumentException("TaskGroup with given id not found"));
        result.setDone(!result.isDone());
        taskGroupRepository.save(result);
    }
}

