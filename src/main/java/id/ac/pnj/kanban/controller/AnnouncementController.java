package id.ac.pnj.kanban.controller;

import com.blazebit.persistence.PagedList;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    public String showAnnouncementList(Model model,
                                       @RequestParam(value = "page") Optional<Integer> page,
                                       @RequestParam(value = "size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(20);
        PagedList<Announcement> announcements = kanbanService.findAllAnnouncements(
                PageRequest.of(currentPage - 1, pageSize));
        System.out.println("Total size: " + announcements.getTotalSize());
        System.out.println("Total page:" + announcements.getTotalPages());

        model.addAttribute("announcements", announcements);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", announcements.getTotalPages());

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

    @PostMapping("/announcements/show-update-announcement-form")
    public String showupdateAnnouncementForm(@RequestParam("announcementId") int announcementId, Model model) {

        return "announcement-form";
    }

}
