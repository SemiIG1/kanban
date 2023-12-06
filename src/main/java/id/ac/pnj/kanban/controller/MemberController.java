package id.ac.pnj.kanban.controller;

import id.ac.pnj.kanban.dto.TeamMemberDTO;
import id.ac.pnj.kanban.entity.Member;
import id.ac.pnj.kanban.service.KanbanService;
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

import java.util.List;

@Controller
public class MemberController {
    private KanbanService kanbanService;


    public MemberController(KanbanService kanbanService) {
        this.kanbanService = kanbanService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {

        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }
    @GetMapping("/members")
    public String findAllMembers(Model model) {
        List<Member> members = kanbanService.findAllMembers();
        model.addAttribute("members", members);
        return "team-list";
    }
    @PostMapping("/members/show-add-member-form/save")
    public String save(@Valid @ModelAttribute("member") TeamMemberDTO teamMemberDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "members/show-add-member-form";
        }

        return "redirect:/members";
    }
    @GetMapping("/members/show-add-member-form")
    public String showAddMemberForm(Model model) {
        model.addAttribute("");
        return "team-member-form";
    }
}
