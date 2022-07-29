package io.github.agamgk1.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface TaskQueryRepository extends Repository<Task, Integer> {

    List<Task> findAll();

    Page<Task> findAll(Pageable pageable);

    boolean existsById(Integer id);

    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);

    List<Task> findByDone(@Param("state") boolean done);

    List<Task> findAllByGroup_Id(Integer groupId);

    Optional<TaskDto> findDtoById(Integer id);
}
