package id.ac.pnj.kanban.restcontroller;

import id.ac.pnj.kanban.dto.EventDTO;
import id.ac.pnj.kanban.entity.Event;
import id.ac.pnj.kanban.service.KanbanService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventRestController {
    public KanbanService kanbanService;

    public EventRestController(KanbanService kanbanService) {
        this.kanbanService = kanbanService;
    }

    @ResponseBody
    @GetMapping("/api/events")
    public List<Event> findAllEvents() {

        return kanbanService.findAllEvents();
    }

}

