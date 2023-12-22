package id.ac.pnj.kanban.controller;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import id.ac.pnj.kanban.dao.KanbanDao;
import id.ac.pnj.kanban.dto.EventDTO;
import id.ac.pnj.kanban.entity.Event;
import id.ac.pnj.kanban.entity.Member;
import id.ac.pnj.kanban.service.KanbanService;
import id.ac.pnj.kanban.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class EventController {
    public KanbanService kanbanService;
    public UserService userService;

    public EventController(KanbanService kanbanService, UserService userService) {
        this.kanbanService = kanbanService;
        this.userService = userService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {

        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @PostMapping("/events/delete")
    public String deleteEvent(@RequestParam("eventId") int eventId) {
        kanbanService.deleteEventById(eventId);
        return "redirect:/events";
    }

    @PostMapping("/events/save")
    public String saveEvent(
            @Valid @ModelAttribute("event") EventDTO eventDTO, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "event-form";
        } else {
            Event event = new Event(
                    eventDTO.getTitle(), eventDTO.getDescription(), eventDTO.getStart(), eventDTO.getEnd());
            Member currentLoggedInMember = userService.findUserByEmail(principal.getName());
            event.setCreatedBy(currentLoggedInMember);
            kanbanService.save(event);
            return "redirect:/events";
        }
    }
    @GetMapping("/events")
    public String showEventCalendar(Model model) {
        List<Event> events = kanbanService.findAllEvents();
        model.addAttribute("events", events);
        return "event-calendar";
    }

    @GetMapping("/events/show-add-event-form")
    public String showAddEventForm(Model model) {
        EventDTO event = new EventDTO();
        model.addAttribute("event", event);
        return "event-form";
    }

    @PostMapping("/events/show-update-event-form")
    public String showUpdateEventForm(@RequestParam("eventId") int eventId, Model model) {
        Event event = kanbanService.findEventById(eventId);
        EventDTO eventDTO = new EventDTO(event.getTitle(), event.getDescription(), event.getStart(), event.getEnd());
        model.addAttribute("event", eventDTO);
        return "event-form";
    }
}
