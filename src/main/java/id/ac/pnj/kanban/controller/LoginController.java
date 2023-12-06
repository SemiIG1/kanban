package id.ac.pnj.kanban.controller;

import id.ac.pnj.kanban.service.KanbanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    private KanbanService kanbanService;


    public LoginController(KanbanService kanbanService) {
        this.kanbanService = kanbanService;
    }

    @GetMapping("/show-login-page")
    public String showLoginPage() {
        return "login-form";
    }
}
