package io.github.agamgk1.taskGroup;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface TaskGroupRepository extends Repository<TaskGroup, Integer> {

    Optional<TaskGroup> findById(Integer id);

    TaskGroup save(TaskGroup entity);

}
