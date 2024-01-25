package id.ac.pnj.kanban.controller;

import com.blazebit.persistence.PagedList;
import id.ac.pnj.kanban.dto.*;
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

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

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
    @PostMapping("/projects/{projectId}/files/delete")
    public String deleteFile(@PathVariable int projectId, @RequestParam("fileId") int fileId) {
        storageService.deleteFileById(fileId);
        return "redirect:/projects/" + projectId + "/files";
    }
    @PostMapping("/projects/{projectId}/notes/delete")
    public String deleteNote(@PathVariable int projectId, @RequestParam("noteId") int noteId) {
        kanbanService.deleteNoteById(noteId);
        return "redirect:/projects/" + projectId + "/notes";
    }
    @PostMapping("/projects/delete")
    public String deleteProject(@RequestParam("projectId") int projectId) {
        kanbanService.deleteProjectById(projectId);
        return "redirect:/projects";
    }

    @PostMapping("/projects/{projectId}/members/delete")
    public String deleteProjectMember(@PathVariable int projectId, @RequestParam("memberId") int memberId) {
        kanbanService.deleteProjectMemberById(projectId, memberId);
        return "redirect:/projects";
    }
    @PostMapping("/projects/{projectId}/tasks/delete")
    public String deleteTask(@PathVariable int projectId, @RequestParam("taskId") int taskId) {
        kanbanService.deleteTaskById(taskId);
        return "redirect:/projects/" + taskId + "/tasks";
    }
    @PostMapping("/projects/{projectId}/files/show-add-file-form/upload-file")
    public String handleFileUpload(
            @PathVariable int projectId, @RequestParam("file") MultipartFile file, Principal principal) {

        Member currentLoggedInMember = userService.findUserByEmail(principal.getName());
        storageService.store(file, projectId, currentLoggedInMember);
        return "redirect:/projects/" + projectId + "/files";
    }
    @PostMapping("/projects/{projectId}/members/show-update-member-form/save")
    public String saveEditedMember(@Valid @ModelAttribute("member") EditProjectMemberDTO editProjectMemberDTO,
                             @PathVariable int projectId, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit-project-member";
        } else {
            Member databaseMember = kanbanService.findMemberById(editProjectMemberDTO.getId());
            databaseMember.setName(editProjectMemberDTO.getName());
            databaseMember.setEmail(editProjectMemberDTO.getEmail());
            databaseMember.setPhone(editProjectMemberDTO.getPhone());
            kanbanService.save(databaseMember);
            return "redirect:/projects/" + projectId;
        }

    }
    @PostMapping("/projects/{projectId}/show-add-member-form/save")
    public String saveMember(@ModelAttribute("member") ProjectMemberDTO projectMemberDTO,
            @PathVariable int projectId) {
        Project project = kanbanService.findProjectById(projectId);
        Member foundMember = kanbanService.findMemberByName(projectMemberDTO.getMember());
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
            Note note;
            if (noteDTO.getId() == 0) {
                note = new Note(noteDTO.getMessage(), LocalDateTime.now());
                note.setId(noteDTO.getId());
                note.setCreatedBy(currentLoggedInMember);
            } else {
                note = kanbanService.findNoteById(noteDTO.getId());
                note.setMessage(noteDTO.getMessage());
                note.setUpdatedBy(currentLoggedInMember);
            }
            kanbanService.save(note);

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

    @PostMapping("/projects/{projectId}/tasks/save")
    public String saveTask(
            @Valid @ModelAttribute("task") TaskDTO taskDTO,
            @PathVariable int projectId,
            BindingResult bindingResult,
            Principal principal){
        System.out.println(taskDTO.getMember());
        if (bindingResult.hasErrors()) {
            return "task-form";
        }
        else {
            Member currentLoggedInMember = kanbanService.findMemberByName(taskDTO.getMember());
            Task task;
            if (taskDTO.getId() == 0) {
                task = new Task(taskDTO.getTitle(), taskDTO.getStartDatetime(), taskDTO.getDeadline());
                task.setCreatedBy(currentLoggedInMember);
            } else {
                task = kanbanService.findTaskById(taskDTO.getId());
                task.setUpdatedBy(currentLoggedInMember);
            }

            Status initialStatus = kanbanService.findStatusByName(taskDTO.getStatus());
            task.setStatus(initialStatus);

            Project project = kanbanService.findProjectById(projectId);
            task.setProject(project);
            kanbanService.save(task);
            int projectProgress = kanbanService.findProjectProgressById(projectId);
            project.setProgress(projectProgress);
            kanbanService.save(project);
            System.out.println("Project progress: " + projectProgress);

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
        NoteDTO note = new NoteDTO();
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

        List<Object[]> members = kanbanService.findAllMembersByProjectId(projectId);
        List<Status> statuses = kanbanService.findAllStatuses();

        model.addAttribute("members", members);
        model.addAttribute("statuses", statuses);
        model.addAttribute("task", new TaskDTO());

        return "task-form";
    }
    @GetMapping("/projects/{projectId}/files")
    public String showFileList(@PathVariable int projectId, Model model) {
        List<FileDTO> fileDTOList = storageService.loadAllFilesFromDatabaseByProjectId(projectId).map(
                file -> {file.setPath(MvcUriComponentsBuilder.fromMethodName(
                    ProjectController.class, "serveFile",
                    Paths.get(file.getPath()).getFileName().toString()).build().toUri().toString());
                return file;
        }).collect(toList());
        model.addAttribute("files", fileDTOList);
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
        List<Object[]> membersNotInProject = kanbanService.findAllMembersNotInProjectWithProjectId(projectId);

        model.addAttribute("member", new ProjectMemberDTO());
        model.addAttribute("membersNotInProject", membersNotInProject);
        return "project-member-form";
    }

    @GetMapping("/projects/{projectId}")
    public String showProjectOverview(@PathVariable int projectId, Model model) {
        List<Object[]> members = kanbanService.findAllMembersByProjectId(projectId);
        List<Task> doneTasks = kanbanService.findAllDoneTasksByProjectId(projectId);
        List<Task> inProgressTasks = kanbanService.findAllInProgressTasksByProjectId(projectId);
        int projectProgress = kanbanService.findProjectProgressById(projectId);
        List<Task> toDoTasks = kanbanService.findAllToDoTasksByProjectId(projectId);

        model.addAttribute("doneTasks", doneTasks);
        model.addAttribute("inProgressTasks", inProgressTasks);
        model.addAttribute("members", members);
        model.addAttribute("projectId", projectId);
        model.addAttribute("projectProgress", projectProgress);
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

    @PostMapping("/projects/{projectId}/members/show-update-member-form")
    public String showUpdateMemberForm(@PathVariable("projectId") int projectId, @RequestParam("memberId") int memberId, Model model) {
        Member member = kanbanService.findMemberById(memberId);
        EditProjectMemberDTO memberDTO = new EditProjectMemberDTO(memberId, member.getName(), member.getEmail(), member.getPhone());
        model.addAttribute("member", memberDTO);
        return "edit-project-member-form";
    }


    @PostMapping("/projects/{projectId}/notes/show-update-note-form")
    public String showUpdateNoteForm(@PathVariable("projectId") int projectId, @RequestParam("noteId") int noteId, Model model) {
        Note note = kanbanService.findNoteById(noteId);

        NoteDTO noteDTO = new NoteDTO(note.getMessage());
        noteDTO.setId(note.getId());

        model.addAttribute("note", noteDTO);
        return "note-form";
    }

    @PostMapping("/projects/show-update-project-form")
    public String showUpdateProjectForm(@RequestParam("projectId") int projectId, Model model) {
        Project project = kanbanService.findProjectById(projectId);
        List<Status> statuses = kanbanService.findAllStatuses();
        model.addAttribute("project", project);
        model.addAttribute("statuses", statuses);
        return "project-form";
    }


     /*
    @PostMapping("/projects/{projectId}/show-update-member-form")
    public String showUpdateProjectMemberForm(@PathVariable("projectId") int projectId, Model model) {
        Project project = kanbanService.findProjectById(projectId);
        List<Status> statuses = kanbanService.findAllStatuses();
        model.addAttribute("project", project);
        model.addAttribute("statuses", statuses);
        return "project-form";
    }

      */
    @PostMapping("/projects/{projectId}/tasks/show-update-task-form")
    public String showUpdateTaskForm(@PathVariable int projectId, @RequestParam("taskId") int taskId, Model model) {
        Task task = kanbanService.findTaskById(taskId);
        TaskDTO taskDTO = new TaskDTO(task.getTitle(), task.getStartDatetime(), task.getDeadline(), task.getStatus().getName());
        taskDTO.setId(task.getId());
        List<Object[]> members = kanbanService.findAllMembersByProjectId(projectId);
        List<Status> statuses = kanbanService.findAllStatuses();
        model.addAttribute("members", members);
        model.addAttribute("statuses", statuses);
        model.addAttribute("task", taskDTO);
        return "task-form";
    }
}
