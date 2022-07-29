package io.github.agamgk1.task;

import org.springframework.data.repository.Repository;
import java.util.Optional;

interface TaskRepository extends Repository<Task, Integer> {

    Optional<Task> findById(Integer id);

    Task save(Task entity);

}
