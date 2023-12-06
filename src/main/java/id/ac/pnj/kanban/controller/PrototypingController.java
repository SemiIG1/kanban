package id.ac.pnj.kanban.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// Used for prototyping Bootstrap design
// Don't use git on this class
@Controller
@RequestMapping("/")
public class PrototypingController {
    @GetMapping("/prototype")
    public String prototype() {
        return "prototype";
    }
}
