package id.ac.pnj.kanban.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.LAZY,
            mappedBy = "createdBy"
    )
    @JsonIgnore
    private List<Announcement> createdAnnouncements;

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.LAZY,
            mappedBy = "createdBy"
    )
    @JsonIgnore
    private List<Announcement> updatedAnnouncements;
    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.LAZY,
            mappedBy = "createdBy"
    )
    @JsonIgnore
    private List<Task> createdTasks;
    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.LAZY,
            mappedBy = "createdBy"
    )
    @JsonIgnore
    private List<Task> updatedTasks;

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.LAZY,
            mappedBy = "createdBy"
    )
    @JsonIgnore
    private List<Event> createdEvents;
    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.LAZY,
            mappedBy = "updatedBy"
    )
    @JsonIgnore
    private List<Event> updatedEvents;
    @OneToMany(
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.LAZY,
            mappedBy = "member"
    )
    @JsonIgnore
    private List<File> files;
    @OneToMany(
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.LAZY,
            mappedBy = "createdBy"
    )
    @JsonIgnore
    private List<Note> createdNotes;

    @OneToMany(
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.LAZY,
            mappedBy = "createdBy"
    )
    @JsonIgnore
    private List<Note> updatedNotes;
    @ManyToMany(
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "project_member",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    @JsonIgnore
    private List<Project> projects;

    // If I have time, I might change this fetch and optimize it
    @ManyToMany(
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "member_role",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonIgnore
    private List<Role> roles;
    private String name;

    private String email;
    private String password;
    private String phone;

    // private int enabled;

    public Member() {

    }

    public Member(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Member(String name, String email, String password, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        // this.enabled = enabled;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Announcement> getCreatedAnnouncements() {
        return createdAnnouncements;
    }

    public void setCreatedAnnouncements(List<Announcement> createdAnnouncements) {
        this.createdAnnouncements = createdAnnouncements;
    }

    public List<Announcement> getUpdatedAnnouncements() {
        return updatedAnnouncements;
    }

    public void setUpdatedAnnouncements(List<Announcement> updatedAnnouncements) {
        this.updatedAnnouncements = updatedAnnouncements;
    }

    public List<Task> getCreatedTasks() {
        return createdTasks;
    }

    public void setCreatedTasks(List<Task> createdTasks) {
        this.createdTasks = createdTasks;
    }

    public List<Task> getUpdatedTasks() {
        return updatedTasks;
    }

    public void setUpdatedTasks(List<Task> updatedTasks) {
        this.updatedTasks = updatedTasks;
    }

    public List<Event> getCreatedEvents() {
        return createdEvents;
    }

    public void setCreatedEvents(List<Event> createdEvents) {
        this.createdEvents = createdEvents;
    }

    public List<Event> getUpdatedEvents() {
        return updatedEvents;
    }

    public void setUpdatedEvents(List<Event> updatedEvents) {
        this.updatedEvents = updatedEvents;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public List<Note> getCreatedNotes() {
        return createdNotes;
    }

    public void setCreatedNotes(List<Note> createdNotes) {
        this.createdNotes = createdNotes;
    }

    public List<Note> getUpdatedNotes() {
        return updatedNotes;
    }

    public void setUpdatedNotes(List<Note> updatedNotes) {
        this.updatedNotes = updatedNotes;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void addAnnouncement(Announcement announcement) {
        if (createdAnnouncements == null) {
            createdAnnouncements = new ArrayList<>();
        }
        createdAnnouncements.add(announcement);
    }

    public void addEvent(Event event) {
        if (createdEvents == null) {
            createdEvents = new ArrayList<>();
        }
        createdEvents.add(event);
    }
    public void addFile(File file) {
        if (files == null) {
            files = new ArrayList<>();
        }
        files.add(file);
    }

    public void addNote(Note note) {
        if (createdNotes == null) {
            createdNotes = new ArrayList<>();
        }
        createdNotes.add(note);
    }

    public void addProject(Project project) {
        if (projects == null) {
            projects = new ArrayList<>();
        }
        projects.add(project);
    }

    public void addRole(Role role) {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        roles.add(role);
    }

    public void addTask(Task task) {
        if (createdTasks == null) {
            createdTasks = new ArrayList<>();
        }
        createdTasks.add(task);
    }
}
