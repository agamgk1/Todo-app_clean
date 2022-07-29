package io.github.agamgk1.project;

import io.micrometer.core.annotation.Timed;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
//@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/projects")
class ProjectController {
    private final ProjectFacade projectFacade;

    ProjectController(ProjectFacade projectFacade) {
        this.projectFacade = projectFacade;
    }

    @GetMapping
    String showProjects(Model model, Authentication auth) {
            model.addAttribute("project", new ProjectDtoWriteModel());
            return "projects";
    }

    @PostMapping
    String addProject(
            @ModelAttribute("project") @Valid ProjectDtoWriteModel current,
            BindingResult bindingResult,
            Model model) {
        if(bindingResult.hasErrors()) {
            return "projects";
        }
        projectFacade.save(current);
        model.addAttribute("project", new ProjectDtoWriteModel());
        model.addAttribute("projects", getProjects());
        model.addAttribute("message", "Dodano projekt");
        return "projects";
    }

    @PostMapping(params = "addStep")
    String addProjectStep(@ModelAttribute("project") ProjectDtoWriteModel current) {
        current.getSteps().add(new ProjectStepDto());
        return "projects";
    }

    @Timed(value = "project.create.group", histogram = true, percentiles = {0.5, 0.95, 0.99})
    @PostMapping("/{id}")
    String createGroup(
            @ModelAttribute("project") ProjectDtoWriteModel current,
            Model model,
            @PathVariable int id,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime deadline) {
        try {
            projectFacade.createGroup(deadline, id);
            model.addAttribute("message", "Dodano grupę");
        } catch (IllegalStateException | IllegalArgumentException e) {
            model.addAttribute("message", "Błąd podczas tworzenia grupy!");
        }
        return "projects";
    }

    @ModelAttribute("projects")
    List<ProjectDtoReadModel> getProjects() {
        return projectFacade.findAll();
    }
}