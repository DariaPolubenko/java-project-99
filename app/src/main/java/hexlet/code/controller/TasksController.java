package hexlet.code.controller;

import hexlet.code.component.TaskSpecification;
import hexlet.code.dto.task.CreateTaskDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskParamsDTO;
import hexlet.code.dto.task.UpdateTaskDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.repository.TaskRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TasksController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskSpecification specBuilder;

    @GetMapping
    public ResponseEntity<List<TaskDTO>> index(TaskParamsDTO params) {
        var spec = specBuilder.build(params);
        var tasks = taskRepository.findAll(spec);
        var tasksDTO = tasks.stream()
                .map(taskMapper::map)
                .toList();
        /*
        try {
            throw new Exception("This is a test.");
        } catch (Exception e) {
            Sentry.captureException(e);
        }
         */

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(tasks.size()))
                .body(tasksDTO);
    }

    @GetMapping("/{id}")
    public TaskDTO show(@PathVariable Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        var taskDTO = taskMapper.map(task);
        return taskDTO;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO create(@Valid @RequestBody CreateTaskDTO data) {
        var task = taskMapper.map(data);
        taskRepository.save(task);
        var taskDTO = taskMapper.map(task);
        return taskDTO;
    }

    @PutMapping("/{id}")
    public TaskDTO update(@PathVariable Long id, @Valid @RequestBody UpdateTaskDTO data) throws AccessDeniedException {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        taskMapper.update(data, task);
        taskRepository.save(task);
        var taskDTO = taskMapper.map(task);
        return taskDTO;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws AccessDeniedException {
        var taskStatus = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        taskRepository.delete(taskStatus);
    }
}
