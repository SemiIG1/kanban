package id.ac.pnj.kanban.dao;

import id.ac.pnj.kanban.entity.*;

import java.util.List;

public interface KanbanDao {
    List<Announcement> findAllAnnouncements();
    List<File> findAllFilesByProjectId(int id);
    List<Member> findAllMembersByProjectId(int id);
    List<Member> findAllMembersNotInProjectWithProjectId(int id);

    List<Note> findAllNotesByProjectId(int id);
    List<Project> findAllProjects();

    List<Role> findAllRoles();
    List<Status> findAllStatuses();
    List<Task> findAllTasksByProjectId(int id);
    List<Member> findAllMembers();

    Member findMemberByEmail(String email);

    Project findProjectById(int id);

    Role findRoleByName(String name);

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
