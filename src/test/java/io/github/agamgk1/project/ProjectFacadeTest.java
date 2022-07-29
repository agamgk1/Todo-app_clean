package io.github.agamgk1.project;

import io.github.agamgk1.configuration.TaskConfigurationProperties;
import io.github.agamgk1.taskGroup.TaskGroupQueryRepository;
import io.github.agamgk1.taskGroup.TaskGroupRepository;
import io.github.agamgk1.taskGroup.TaskGroupDtoReadModel;
import io.github.agamgk1.taskGroup.TaskGroup;
import io.github.agamgk1.taskGroup.TaskGroupFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectFacadeTest {

    @Test
    @DisplayName("should throws IllegalStateException when configured to allow just 1 group and the other undone group exists ")
    void createGroup_noMultipleGroupsConfig_and_undoneGroupsExists_throwsIllegalStateException() {
        //given
        var mockGroupRepository = mock(TaskGroupQueryRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(true);
        //and
        TaskConfigurationProperties mockConfig = configurationReturning(false);
        //system under test
        var toTest = new ProjectFacade(null, null, mockGroupRepository, null, mockConfig);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("one undone group");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when configured ok and no projects for given id")
    void createGroup_ConfigurationOK_and_noProjects_throwsIllegalArgumentException() {
        //given
        var mockProjectRepository = mock(ProjectRepository.class);
        when(mockProjectRepository.findById(anyInt())).thenReturn(Optional.empty());
        //and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        //system under test
        var toTest = new ProjectFacade(mockProjectRepository, null,null, null, mockConfig);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Project with given id not found");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when configured to allow just 1 group and no groups and no projects")
    void createGroup_noMultipleGroupsConfig_and_noUndoneGroupExists_noProjects_throwsIllegalArgumentException() {
        //given
        var mockProjectRepository = mock(ProjectRepository.class);
        when(mockProjectRepository.findById(anyInt())).thenReturn(Optional.empty());
        //and
        var mockTaskGroupQueryRepository = mock(TaskGroupQueryRepository.class);
        when(mockTaskGroupQueryRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(false);
        //and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        //system under test
        var toTest = new ProjectFacade(mockProjectRepository, null, mockTaskGroupQueryRepository, null, mockConfig);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Project with given id not found");
    }

    @Test
    @DisplayName("should create new group from project")
    void createGroup_configurationOK_existingProject_createsAndSavesGroup() {
        //given
        var today = LocalDate.now().atStartOfDay();
        var mockProjectRepository = mock(ProjectRepository.class);
        var project =   projectWith("bar", Set.of(-1, -2));
        when(mockProjectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        //and
        InMemoryGroupRepository inMemoryGroupRepo = inMemoryGroupRepository();
        var serviceWithInMemRepo = new TaskGroupFacade(inMemoryGroupRepo, null, null);
        int countBeforeCall = inMemoryGroupRepo.count();
        //and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        //system under test
        var toTest = new ProjectFacade(mockProjectRepository, null, null, serviceWithInMemRepo, mockConfig);

        //when
        TaskGroupDtoReadModel result = toTest.createGroup(today, 1);
        //then
       assertThat(result.getDescription()).isEqualTo("bar");
       assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
       assertThat(result.getTasks())
               .allMatch(groupTaskReadModel -> groupTaskReadModel.getDescription().equals("foo"));
       assertThat(countBeforeCall + 1).isEqualTo(inMemoryGroupRepo.count());

    }

    //metoda zwracajaca projekt
    private Project projectWith(String description, Set<Integer> daysToDeadline) {
        Set<ProjectStep> steps = daysToDeadline.stream()
                .map(days -> {
                    var step = mock(ProjectStep.class);
                    when(step.getDescription()).thenReturn("foo");
                    when(step.getDaysToDeadline()).thenReturn(days);
                    return step;
                }).collect(Collectors.toSet());
        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(description);
        when(result.getSteps()).thenReturn(steps);
        return result;
    }

    private TaskConfigurationProperties configurationReturning(boolean b) {
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(b);
        //and
        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        return mockConfig;
    }

    private InMemoryGroupRepository inMemoryGroupRepository() {
       return new InMemoryGroupRepository() ;
    }

    public static class InMemoryGroupRepository implements TaskGroupRepository {

            private int index = 0;
            private Map<Integer, TaskGroup> map = new HashMap<>();

            public int count() {
                return map.values().size();
            }

            @Override
            public Optional<TaskGroup> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
        }


            @Override
            public TaskGroup save(TaskGroup entity) {
            if (entity.getId() == 0) {
                try {
                 var filed =   TaskGroup.class.getDeclaredField("id");
                 filed.setAccessible(true);
                 filed.set(entity, ++index);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }
            }
            map.put(entity.getId(), entity);
            return entity;
        }

    }
}




