package id.ac.pnj.kanban.controller;

import com.blazebit.persistence.PagedList;
import id.ac.pnj.kanban.dto.NoteDTO;
import id.ac.pnj.kanban.dto.ProjectDTO;
import id.ac.pnj.kanban.dto.TaskDTO;
import id.ac.pnj.kanban.entity.*;
import id.ac.pnj.kanban.service.KanbanService;
import id.ac.pnj.kanban.service.UserService;
import id.ac.pnj.kanban.storage.StorageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ProjectController {
    private KanbanService kanbanService;

    private final StorageService storageService;

    private UserService userService;

    public ProjectController(KanbanService kanbanService, StorageService storageService, UserService userService) {
        this.kanbanService = kanbanService;
        this.storageService = storageService;
        this.userService = userService;
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }
    @PostMapping("/projects/{projectId}/files/show-add-file-form/upload-file")
    public String handleFileUpload(
            @PathVariable int projectId, @RequestParam("file") MultipartFile file, Principal principal) {

        Member currentLoggedInMember = userService.findUserByEmail(principal.getName());
        storageService.store(file, projectId, currentLoggedInMember);
        return "redirect:/projects/" + projectId + "/files";
    }
    @PostMapping("/projects/{projectId}/show-add-member-form/save")
    public String saveMember(@ModelAttribute("member") Member member,
            @PathVariable int projectId) {
        Project project = kanbanService.findProjectById(projectId);
        Member foundMember = kanbanService.findMemberByName(member.getName());
        project.addMember(foundMember);
        kanbanService.save(project);
        return "redirect:/projects/" + projectId;

    }
    @PostMapping("/projects/{projectId}/notes/show-add-note-form/save")
    public String saveNote(
            @Valid @ModelAttribute("note") NoteDTO noteDTO,
            @PathVariable int projectId,
            BindingResult bindingResult,
            Principal principal) {
        if (bindingResult.hasErrors()) {
            return "note-form";
        }
        else {
            Member currentLoggedInMember = userService.findUserByEmail(principal.getName());
            Note note = new Note(noteDTO.getMessage(), LocalDateTime.now());
            note.setMember(currentLoggedInMember);
            Project project = kanbanService.findProjectById(projectId);
            note.setProject(project);
            kanbanService.save(note);
            return "redirect:/projects/" + projectId + "/notes";
        }
    }
    @PostMapping("/projects/save")
    public String saveProject(@Valid @ModelAttribute("project") ProjectDTO projectDTO, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            List<Status> statuses = kanbanService.findAllStatuses();
            model.addAttribute("statuses", statuses);
            return "project-form";
        } else {
            Project project = new Project(
                    projectDTO.getProject(),
                    projectDTO.getCustomer(),
                    projectDTO.getStartUpdate(),
                    projectDTO.getDeadline(),
                    0
            );
            Status status = kanbanService.findStatusByName(projectDTO.getStatus());
            project.setStatus(status);

            kanbanService.save(project);
            return "redirect:/projects";
        }
    }

    @PostMapping("/projects/{projectId}/tasks/show-add-task-form/save")
    public String saveTask(
            @Valid @ModelAttribute("task") TaskDTO taskDTO,
            @PathVariable int projectId,
            BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "task-form";
        }
        else {
            Task task = new Task(taskDTO.getTitle(), taskDTO.getStartDatetime(), taskDTO.getDeadline());
            Member selectedMember = kanbanService.findMemberByName(taskDTO.getMember());
            task.setMember(selectedMember);
            Status initialStatus = kanbanService.findStatusByName(taskDTO.getStatus());
            task.setStatus(initialStatus);
            Project project = kanbanService.findProjectById(projectId);
            task.setProject(project);
            kanbanService.save(task);
            return "redirect:/projects/" + projectId + "/tasks";
        }
    }

    @GetMapping("/projects/{projectId}/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);

        if (file == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
    @GetMapping("/projects/{projectId}/files/show-add-file-form")
    public String showAddFileForm(@PathVariable int projectId, Model model) {
        File file = new File();
        model.addAttribute("file", file);
        return "file-form";
    }

    @GetMapping("/projects/{projectId}/notes/show-add-note-form")
    public String showAddNoteForm(@PathVariable int projectId, Model model) {
        Note note = new Note();
        model.addAttribute("note", note);
        model.addAttribute("projectId", projectId);
        return "note-form";
    }
    @GetMapping("/projects/show-add-project-form")
    public String showAddProjectForm(Model model) {
        Project project = new Project();
        List<Status> statuses = kanbanService.findAllStatuses();
        model.addAttribute("project", project);
        model.addAttribute("statuses", statuses);
        return "project-form";
    }
    @GetMapping("/projects/{projectId}/tasks/show-add-task-form")
    public String showAddTaskForm(@PathVariable int projectId, Model model) {
        Task task = new Task();
        List<Member> members = kanbanService.findAllMembersByProjectId(projectId);
        List<Status> statuses = kanbanService.findAllStatuses();
        model.addAttribute("members", members);
        model.addAttribute("statuses", statuses);
        model.addAttribute("task", task);

        return "task-form";
    }
    @GetMapping("/projects/{projectId}/files")
    public String showFileList(@PathVariable int projectId, Model model) {
        List<File> files = kanbanService.findAllFilesByProjectId(projectId);
        Project project = kanbanService.findProjectById(projectId);
        // model.addAttribute("files", files);
        model.addAttribute("files", storageService.loadAll().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(ProjectController.class,
                                "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));
        model.addAttribute("projectId", projectId);
        return "file-list";
    }
    @GetMapping("/projects/{projectId}/notes")
    public String showNoteList(@PathVariable int projectId, Model model,
                               @RequestParam(value = "page") Optional<Integer> page,
                               @RequestParam(value = "size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(20);
        PagedList<Note> notes = kanbanService
                .findAllNotesByProjectId(projectId, PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("notes", notes);
        model.addAttribute("projectId", projectId);
        model.addAttribute("totalPages", notes.getTotalPages());
        return "note-list";
    }

    @GetMapping("/projects")
    public String showProjectList(Model model,
                                  @RequestParam(value = "page") Optional<Integer> page,
                                  @RequestParam(value = "size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(20);
        PagedList<Project> projects = kanbanService.findAllProjects(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("projects", projects);
        model.addAttribute("totalPages", projects.getTotalPages());
        return "project-list";
    }

    @GetMapping("/projects/{projectId}/show-add-member-form")
    public String showProjectMemberForm(@PathVariable int projectId, Model model) {
        List<Member> membersNotInProject = kanbanService.findAllMembersNotInProjectWithProjectId(projectId);
        model.addAttribute("member", new Member());
        model.addAttribute("membersNotInProject", membersNotInProject);
        return "project-member-form";
    }

    @GetMapping("/projects/{projectId}")
    public String showProjectOverview(@PathVariable int projectId, Model model) {
        List<Member> members = kanbanService.findAllMembersByProjectId(projectId);
        List<Task> doneTasks = kanbanService.findAllDoneTasksByProjectId(projectId);
        List<Task> inProgressTasks = kanbanService.findAllInProgressTasksByProjectId(projectId);
        List<Task> toDoTasks = kanbanService.findAllToDoTasksByProjectId(projectId);

        model.addAttribute("doneTasks", doneTasks);
        model.addAttribute("inProgressTasks", inProgressTasks);
        model.addAttribute("members", members);
        model.addAttribute("projectId", projectId);
        model.addAttribute("toDoTasks", toDoTasks);

        return "project-overview";
    }

    @GetMapping("/projects/{projectId}/tasks")
    public String showTaskList(@PathVariable int projectId, Model model,
                               @RequestParam(value = "page") Optional<Integer> page,
                               @RequestParam(value = "size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(20);
        PagedList<Task> tasks = kanbanService.findAllTasksByProjectId(projectId, PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("projectId", projectId);
        model.addAttribute("tasks", tasks);
        model.addAttribute("totalPages", tasks.getTotalPages());
        return "task-list";
    }
}
