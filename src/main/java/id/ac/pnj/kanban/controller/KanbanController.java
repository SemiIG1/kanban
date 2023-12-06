package id.ac.pnj.kanban.controller;

import id.ac.pnj.kanban.entity.*;
import id.ac.pnj.kanban.service.KanbanService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api")
public class KanbanController {
    private KanbanService kanbanService;

    public KanbanController(KanbanService kanbanService) {
        this.kanbanService = kanbanService;
    }













    public void save(Announcement announcement) {
        kanbanService.save(announcement);
    }

    public void save(File file) {
        kanbanService.save(file);
    }

    public void save(Member member) {
        kanbanService.save(member);
    }

    public void save(Note note) {
        kanbanService.save(note);
    }

    public void save(Project project) {
        kanbanService.save(project);
    }

    public void save(Task task) {
        kanbanService.save(task);
    }
}
