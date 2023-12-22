package id.ac.pnj.kanban.controller;

import id.ac.pnj.kanban.entity.Task;
import id.ac.pnj.kanban.service.KanbanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    private KanbanService kanbanService;

    public HomeController(KanbanService kanbanService) {
        this.kanbanService = kanbanService;
    }

    @GetMapping("/index")
    public String showDashboardPage(Model model) {
        List<Task> tasks = kanbanService.findFirst3TasksOrderedByUpdatedAtDesc();
        model.addAttribute("tasks", tasks);
        return "index";
    }
}
