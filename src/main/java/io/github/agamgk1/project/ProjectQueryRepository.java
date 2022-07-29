package io.github.agamgk1.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface ProjectQueryRepository extends Repository<Project, Integer> {

    @Query("select distinct p from Project p join fetch p.steps")
    List<Project> findAll();

    Page<Project> findAll(Pageable pageable);
}
