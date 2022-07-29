package io.github.agamgk1.taskGroup;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface TaskGroupQueryRepository extends Repository<TaskGroup, Integer> {

    @Query("select distinct g from TaskGroup g join fetch g.tasks")
    List<TaskGroup> findAll();

    boolean existsByDoneIsFalseAndProject_Id(Integer groupId);

    boolean existsByDescription(String description);
}
