package io.github.agamgk1.taskGroup;

import io.github.agamgk1.task.TaskQueryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupFacadeTest {

    @Test
    @DisplayName("should throw IllegalStateException when undone tasks exists")
    void toggleGroup_undone_tasks_exists_throws_IllegalStateException() {
        //given
        var mockTaskQueryRepository = mock(TaskQueryRepository.class);
        when(mockTaskQueryRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(true);
        //system under test
        var toTest = new TaskGroupFacade(null, null, mockTaskQueryRepository);
        //when/then
        var exception = catchThrowable(() -> toTest.toggleGroup(1));
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Group has undone tasks");
    }
    @Test
    @DisplayName("Should throw IllegalArgumentException when TaskGroup with given id not found")
    void toggleGroup_noTaskGroups_throws_IllegalArgumentException() {
        //given
        var mockTaskQueryRepository = mock(TaskQueryRepository.class);
        when(mockTaskQueryRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(false);
        //and
        var mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.empty());
        //system under test
        var toTest = new TaskGroupFacade(mockTaskGroupRepository, null, mockTaskQueryRepository);
        //when + then
        var exception = catchThrowable(() -> toTest.toggleGroup(1));
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("given id not found");

    }
    @Test
    @DisplayName("should toggle TaskGroup with given id")
    void toggleGroup_with_existingTaskGroup() {
        //given
        var taskGroup = new TaskGroup();

        var mockTaskQueryRepository = mock(TaskQueryRepository.class);
        when(mockTaskQueryRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(false);
        //and
        var mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.of(taskGroup));
        //system under test
        var toTest = new TaskGroupFacade(mockTaskGroupRepository, null, mockTaskQueryRepository);
        //then
        boolean beforeToggle = taskGroup.isDone();
        toTest.toggleGroup(taskGroup.getId());
        boolean afterToggle = taskGroup.isDone();
        assertThat(beforeToggle).isNotEqualTo(afterToggle);
    }
}