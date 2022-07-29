package io.github.agamgk1.taskGroup.dto;

import io.github.agamgk1.task.Task;
import io.github.agamgk1.taskGroup.TaskGroup;
import io.github.agamgk1.taskGroup.TaskGroupDtoReadModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class GroupReadModelTest {

    @Test
    @DisplayName("should create null deadline for group when no deadlines")
    void constructor_noDeadlines_createsNullDeadline() {
        //given
        var source = new TaskGroup();
        source.setDescription("foo");
        source.setTasks(Set.of(new Task("bar", null)));

        //when
        var result = new TaskGroupDtoReadModel(source);
        //then
        assertThat(result).hasFieldOrPropertyWithValue("deadline", null);

    }

}