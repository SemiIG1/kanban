package id.ac.pnj.kanban.controller;

import id.ac.pnj.kanban.entity.Member;
import id.ac.pnj.kanban.entity.Role;
import id.ac.pnj.kanban.service.KanbanService;
import id.ac.pnj.kanban.service.OTPService;
import id.ac.pnj.kanban.service.UserService;
import id.ac.pnj.kanban.user.WebUser;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/register")
public class RegisterController {
    private KanbanService kanbanService;
    private UserService userService;

    private OTPService otpService;


    public RegisterController(KanbanService kanbanService, UserService userService, OTPService otpService) {
        this.kanbanService = kanbanService;
        this.userService = userService;
        this.otpService = otpService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {

        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/show-register-form")
    public String showRegisterForm(Model model) {
        List<Role> roles = kanbanService.findAllRoles();
        model.addAttribute("roles", roles);
        model.addAttribute("webUser", new WebUser());
        return "register-form";
    }

    @PostMapping("/process-register-form")
    public String processRegisterForm(
            @Valid @ModelAttribute("webUser") WebUser theWebUser,
            BindingResult theBindingResult,
            HttpSession session, Model theModel) throws MessagingException {

        String userName = theWebUser.getEmail();

        // form validation
        if (theBindingResult.hasErrors()){
            return "register-form";
        }

        // check the database if user already exists
        Member existing = userService.findUserByEmail(userName);
        if (existing != null){
            theModel.addAttribute("webUser", new WebUser());
            theModel.addAttribute("registrationError", "User name already exists.");

            return "register-form";
        }


        userService.save(theWebUser);
        // Generate OTP
        // otpService.generateOtp(userName);

        // place user in the web http session for later use
        session.setAttribute("user", theWebUser);

        return "login-form";
        // return "email-verification";
    }

    @PostMapping("/process-register-form/validate")
    public String validate(@RequestParam("otp") int otp, Model model) {
        WebUser theWebUser = (WebUser) model.getAttribute("user");
        if (otpService.invalidateOtp(theWebUser.getEmail(), otp)) {
            // create user account and store in the database
            // userService.save(theWebUser);
            return "login-form";
        } else {
            return "email-verification";
        }

    }

}
