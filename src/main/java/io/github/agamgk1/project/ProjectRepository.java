package io.github.agamgk1.project;

import org.springframework.data.repository.Repository;

import java.util.Optional;

interface ProjectRepository extends Repository<Project, Integer> {

    Optional<Project> findById(Integer id);

    Project save(Project entity);


}
