package id.ac.pnj.kanban.controller;

import id.ac.pnj.kanban.entity.File;
import id.ac.pnj.kanban.entity.Project;
import id.ac.pnj.kanban.service.KanbanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class FileController {
    private KanbanService kanbanService;


    public FileController(KanbanService kanbanService) {
        this.kanbanService = kanbanService;
    }



    @GetMapping("/files/show-add-file-form")
    public String showAddFileForm(Model model) {
        File file = new File();
        model.addAttribute("file", file);
        return "file-form";
    }
}
