package id.ac.pnj.kanban.dao;

import com.blazebit.persistence.PagedList;
import id.ac.pnj.kanban.entity.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface KanbanDao {
    void deleteAnnouncementById(int id);
    void deleteEventById(int id);
    void deleteFileById(int id);
    void deleteProjectById(int id);

    void deleteProjectMemberById(int projectId, int memberId);
    void deleteNoteById(int id);
    void deleteTaskById(int id);
    PagedList<Announcement> findAllAnnouncements(Pageable pageable);
    Announcement findAnnouncementById(int id);
    List<Task> findAllDoneTasksByProjectId(int id);
    List<Event> findAllEvents();
    List<File> findAllFilesByProjectId(int id);
    List<Task> findAllInProgressTasksByProjectId(int id);

    List<Object[]> findAllMembersByProjectId(int id);
    List<Object[]> findAllMembersNotInProjectWithProjectId(int id);

    PagedList<Note> findAllNotesByProjectId(int id, Pageable pageable);
    PagedList<Project> findAllProjects(Pageable pageable);

    List<Role> findAllRoles();
    List<Status> findAllStatuses();
    List<Task> findAllTasksByProjectId(int id);
    PagedList<Task> findAllTasksByProjectId(int id, Pageable pageable);

    List<Task> findAllToDoTasksByProjectId(int id);
    List<Member> findAllMembers();
    Event findEventById(int id);
    File findFileById(int id);
    List<Task> findAllTasksOrderedByUpdatedAtDesc();
    Member findMemberById(int id);

    Member findMemberByEmail(String email);

    Member findMemberByName(String name);

    Note findNoteById(int id);
    Project findProjectById(int id);
    int findProjectProgressById(int id);

    Role findRoleByName(String name);

    Status findStatusByName(String name);

    Task findTaskById(int id);
    void save(Announcement announcement);
    void save(Event event);
    void save(File file);

    void save(Member member);

    void save(Note note);

    void save(Project project);

    void save(Task task);

    void update(Project project);

    void update(Task task);

}
