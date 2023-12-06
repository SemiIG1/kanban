package id.ac.pnj.kanban.controller;

import id.ac.pnj.kanban.entity.Note;
import id.ac.pnj.kanban.entity.Task;
import id.ac.pnj.kanban.service.KanbanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class TaskController {
    private KanbanService kanbanService;


    public TaskController(KanbanService kanbanService) {
        this.kanbanService = kanbanService;
    }



    @GetMapping("/tasks/show-add-task-form")
    public String showAddTaskForm(Model model) {
        Task task = new Task();
        model.addAttribute("task", task);
        return "task-form";
    }
}
