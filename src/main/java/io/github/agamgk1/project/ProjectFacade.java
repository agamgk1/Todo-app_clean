package io.github.agamgk1.project;

import io.github.agamgk1.configuration.TaskConfigurationProperties;
import io.github.agamgk1.taskGroup.TaskGroupQueryRepository;
import io.github.agamgk1.taskGroup.TaskGroupFacade;
import io.github.agamgk1.taskGroup.TaskGroupDtoReadModel;
import io.github.agamgk1.task.TaskDtoGroupWriteModel;
import io.github.agamgk1.taskGroup.TaskGroupDtoWriteModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectFacade {
    private final ProjectRepository projectRepository;
    private final ProjectQueryRepository projectQueryRepository;
    private final TaskGroupQueryRepository taskGroupQueryRepository;
    private final TaskGroupFacade taskGroupFacade;
    private final TaskConfigurationProperties config;

    public ProjectFacade(ProjectRepository projectRepository, ProjectQueryRepository projectQueryRepository, TaskGroupQueryRepository taskGroupQueryRepository, TaskGroupFacade taskGroupFacade, TaskConfigurationProperties config) {
        this.projectRepository = projectRepository;
        this.projectQueryRepository = projectQueryRepository;
        this.taskGroupQueryRepository = taskGroupQueryRepository;
        this.taskGroupFacade = taskGroupFacade;
        this.config = config;
    }

    List<ProjectDtoReadModel> findAll() {
        return projectQueryRepository.findAll()
                .stream().map(ProjectDtoReadModel::new).collect(Collectors.toList());
    }

    Project save(ProjectDtoWriteModel toSave) {
        return projectRepository.save(toSave.toProject());
    }

    public TaskGroupDtoReadModel createGroup(LocalDateTime deadline, int projectId) {

        if (!config.getTemplate().isAllowMultipleTasks() && taskGroupQueryRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }
        return projectRepository.findById(projectId)
                .map(project -> {
                    var targetGroup = new TaskGroupDtoWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks
                            (project.getSteps()
                                    .stream()
                                    .map(ProjectStep -> {
                                                var task = new TaskDtoGroupWriteModel();
                                                task.setDescription(ProjectStep.getDescription());
                                                task.setDeadline(deadline.plusDays(ProjectStep.getDaysToDeadline()));
                                                return task;
                                            }
                                    ).collect(Collectors.toList())
                            );
                    return taskGroupFacade.createGroup(targetGroup, project);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
    }
}