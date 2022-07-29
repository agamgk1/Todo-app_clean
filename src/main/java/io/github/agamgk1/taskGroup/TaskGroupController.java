package io.github.agamgk1.taskGroup;

import io.github.agamgk1.task.Task;
import io.github.agamgk1.task.TaskQueryRepository;
import io.github.agamgk1.task.TaskDtoGroupWriteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@IllegalExceptionProcessing
@Controller
@RequestMapping("/groups")
class TaskGroupController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskGroupController.class);
    private final TaskQueryRepository taskQueryRepository;
    private final TaskGroupFacade taskGroupFacade;

    TaskGroupController(TaskQueryRepository taskQueryRepository, TaskGroupFacade taskGroupFacade) {
        this.taskQueryRepository = taskQueryRepository;
        this.taskGroupFacade = taskGroupFacade;
    }

    @GetMapping(produces = MediaType.APPLICATION_ATOM_XML_VALUE)
    String showGroups(Model model) {
        model.addAttribute("group",new TaskGroupDtoWriteModel());
        return "groups";
    }

    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<TaskGroupDtoReadModel>> readAllGroups() {
        return ResponseEntity.ok(taskGroupFacade.readAll());
    }

    @ResponseBody
    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Task>> readAllTaskFromGroup(@PathVariable int id) {
        return ResponseEntity.ok(taskQueryRepository.findAllByGroup_Id(id));
    }

    @PostMapping(produces = MediaType.TEXT_HTML_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String addGroup(
            @ModelAttribute("group") @Valid TaskGroupDtoWriteModel current,
            BindingResult bindingResult,
            Model model) {
        if(bindingResult.hasErrors()) {
            return "groups";
        }
        taskGroupFacade.createGroup(current);
        model.addAttribute("group", new TaskGroupDtoWriteModel());
        model.addAttribute("groups", getGroups());
        model.addAttribute("message", "Dodano grupę");
        return "groups";
    }

    @PostMapping(params = "addTask", produces = MediaType.TEXT_HTML_VALUE)
    String addGroupTask(@ModelAttribute("group") TaskGroupDtoWriteModel current) {
        current.getTasks().add(new TaskDtoGroupWriteModel());
        return "groups";
    }

    @ResponseBody
    @Transactional
    @PatchMapping("/{id}")
    ResponseEntity<TaskGroupDtoReadModel> toggleGroup(@PathVariable int id) {
        taskGroupFacade.toggleGroup(id);
        return ResponseEntity.noContent().build(); //202
    }
    @ResponseBody
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<TaskGroupDtoReadModel> createGroup(@RequestBody @Valid TaskGroupDtoWriteModel toCreate) {
       var result = taskGroupFacade.createGroup(toCreate);
        return ResponseEntity.created(URI.create("/"+result.getId())).body(result);
    }

    @ModelAttribute("groups")
    public List<TaskGroupDtoReadModel> getGroups() {
        return taskGroupFacade.readAll();
    }
}
