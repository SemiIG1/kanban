package id.ac.pnj.kanban.controller;

import id.ac.pnj.kanban.dto.AnnouncementDTO;
import id.ac.pnj.kanban.dto.ProjectDTO;
import id.ac.pnj.kanban.entity.Announcement;
import id.ac.pnj.kanban.entity.Member;
import id.ac.pnj.kanban.entity.Project;
import id.ac.pnj.kanban.entity.Status;
import id.ac.pnj.kanban.service.KanbanService;
import id.ac.pnj.kanban.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class AnnouncementController {
    private KanbanService kanbanService;

    private UserService userService;

    public AnnouncementController(KanbanService kanbanService, UserService userService) {
        this.kanbanService = kanbanService;
        this.userService = userService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {

        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @PostMapping("/announcements/save")
    public String saveAnnouncement(
            @Valid @ModelAttribute("announcement") AnnouncementDTO announcementDTO,
            BindingResult bindingResult,
            Principal principal) {
        System.out.println(announcementDTO.getId());
        System.out.println(announcementDTO.getMessage());
        if (bindingResult.hasErrors()) {
            return "announcement-form";
        }
        else {
            Member currentLoggedInMember = userService.findUserByEmail(principal.getName());
            Announcement announcement = new Announcement(announcementDTO.getMessage(), LocalDateTime.now());
            announcement.setMember(currentLoggedInMember);
            kanbanService.save(announcement);
            return "redirect:/announcements";
        }

    }
    @GetMapping("/announcements")
    public String showAnnouncementList(Model model) {
        List<Announcement> announcements = kanbanService.findAllAnnouncements();
        model.addAttribute("announcements", announcements);
        return "announcement-list";
    }
    @GetMapping("/announcements/show-add-announcement-form")
    public String showAddAnnouncementForm(Model model) {
        Announcement announcement = new Announcement();
        List<Status> statuses = kanbanService.findAllStatuses();
        model.addAttribute("announcement", announcement);
        model.addAttribute("statuses", statuses);
        return "announcement-form";
    }


}
