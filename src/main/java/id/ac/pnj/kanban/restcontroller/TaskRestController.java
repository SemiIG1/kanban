package id.ac.pnj.kanban.restcontroller;

import id.ac.pnj.kanban.dto.TaskDragAndDropDTO;
import id.ac.pnj.kanban.entity.Project;
import id.ac.pnj.kanban.entity.Status;
import id.ac.pnj.kanban.entity.Task;
import id.ac.pnj.kanban.response.ProjectResponse;
import id.ac.pnj.kanban.service.KanbanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class TaskRestController {
    private KanbanService kanbanService;

    public TaskRestController(KanbanService kanbanService) {
        this.kanbanService = kanbanService;
    }

    @CrossOrigin
    @PutMapping("/api/tasks")
    public ResponseEntity<Void> updateTask(@Valid @RequestBody TaskDragAndDropDTO taskDragAndDropDTO) {
        Status status = kanbanService.findStatusByName(taskDragAndDropDTO.getStatus());
        Task task = kanbanService.findTaskById(taskDragAndDropDTO.getId());
        task.setStatus(status);
        kanbanService.save(task);

        return ResponseEntity.ok().build();
    }

    @CrossOrigin
    @ResponseBody
    @GetMapping("/api/projects/{projectId}")
    public ProjectResponse getProjectProgress(@PathVariable int projectId) {
        Project project = kanbanService.findProjectById(projectId);
        int projectProgress = kanbanService.findProjectProgressById(projectId);
        project.setProgress(projectProgress);
        kanbanService.save(project);
        return new ProjectResponse(kanbanService.findProjectProgressById(projectId));
    }


}
