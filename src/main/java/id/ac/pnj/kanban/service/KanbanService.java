package id.ac.pnj.kanban.service;

import id.ac.pnj.kanban.entity.*;

import java.util.List;

public interface KanbanService {
    List<Announcement> findAllAnnouncements();
    List<File> findAllFilesByProjectId(int id);

    List<Member> findAllMembersByProjectId(int id);
    List<Member> findAllMembersNotInProjectWithProjectId(int id);
    List<Note> findAllNotesByProjectId(int id);
    List<Member> findAllMembers();
    List<Project> findAllProjects();
    List<Status> findAllStatuses();
    List<Task> findAllTasksByProjectId(int id);

    List<Role> findAllRoles();
    Project findProjectById(int id);

    Status findStatusByName(String name);
    void save(Announcement announcement);
    void save(File file);

    void save(Member member);

    void save(Note note);

    void save(Project project);

    void save(Task task);

    void update(Project project);

    void update(Task task);
}
