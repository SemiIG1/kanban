package id.ac.pnj.kanban.controller;

import id.ac.pnj.kanban.dto.NoteDTO;
import id.ac.pnj.kanban.entity.Announcement;
import id.ac.pnj.kanban.entity.File;
import id.ac.pnj.kanban.entity.Member;
import id.ac.pnj.kanban.entity.Note;
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
public class NoteController {
    private KanbanService kanbanService;

    private UserService userService;

    public NoteController(KanbanService kanbanService, UserService userService) {
        this.kanbanService = kanbanService;
        this.userService = userService;
    }
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {

        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }


    @PostMapping("/notes/save")
    public String saveNote(
            @Valid @ModelAttribute("note") NoteDTO noteDTO,
            BindingResult bindingResult,
            Principal principal) {
        if (bindingResult.hasErrors()) {
            return "note-form";
        }
        else {
            Member currentLoggedInMember = userService.findUserByEmail(principal.getName());
            Note note = new Note(noteDTO.getMessage(), LocalDateTime.now());
            note.setMember(currentLoggedInMember);
            kanbanService.save(note);
            return "redirect:/notes";
        }
    }
    @GetMapping("/notes/show-add-note-form")
    public String showAddNoteForm(Model model) {
        Note note = new Note();
        model.addAttribute("note", note);
        return "note-form";
    }

}
