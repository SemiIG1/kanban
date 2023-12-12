package id.ac.pnj.kanban.service;

import com.blazebit.persistence.PagedList;
import id.ac.pnj.kanban.dao.KanbanDao;
import id.ac.pnj.kanban.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KanbanServiceImpl implements KanbanService {
    private KanbanDao kanbanDao;

    public KanbanServiceImpl(KanbanDao kanbanDao) {
        this.kanbanDao = kanbanDao;
    }

    @Override
    public PagedList<Announcement> findAllAnnouncements(Pageable pageable) {
        return kanbanDao.findAllAnnouncements(pageable);
    }

    @Override
    public Announcement findAnnouncementById(int id) {
        return kanbanDao.findAnnouncementById(id);
    }

    @Override
    public List<Task> findAllDoneTasksByProjectId(int id) {
        return kanbanDao.findAllDoneTasksByProjectId(id);
    }

    @Override
    public List<File> findAllFilesByProjectId(int id) {
        return kanbanDao.findAllFilesByProjectId(id);
    }

    @Override
    public List<Task> findAllInProgressTasksByProjectId(int id) {
        return kanbanDao.findAllInProgressTasksByProjectId(id);
    }

    @Override
    public List<Member> findAllMembersByProjectId(int id) {
        return kanbanDao.findAllMembersByProjectId(id);
    }

    @Override
    public List<Member> findAllMembersNotInProjectWithProjectId(int id) {
        return kanbanDao.findAllMembersNotInProjectWithProjectId(id);
    }

    @Override
    public List<Member> findAllMembers() {
        return kanbanDao.findAllMembers();
    }

    @Override
    public PagedList<Note> findAllNotesByProjectId(int id, Pageable pageable) {
        return kanbanDao.findAllNotesByProjectId(id, pageable);
    }

    @Override
    public PagedList<Project> findAllProjects(Pageable pageable) {
        return kanbanDao.findAllProjects(pageable);
    }

    @Override
    public List<Status> findAllStatuses() {
        return kanbanDao.findAllStatuses();
    }

    @Override
    public List<Task> findAllTasksByProjectId(int id) {
        return kanbanDao.findAllTasksByProjectId(id);
    }

    @Override
    public List<Task> findAllToDoTasksByProjectId(int id) {
        return kanbanDao.findAllToDoTasksByProjectId(id);
    }

    @Override
    public PagedList<Task> findAllTasksByProjectId(int id, Pageable pageable) {
        return kanbanDao.findAllTasksByProjectId(id, pageable);
    }

    @Override
    public List<Role> findAllRoles() {
        return kanbanDao.findAllRoles();
    }

    @Override
    public File findFileById(int id) {
        return null;
    }

    @Override
    public Member findMemberByName(String name) {
        return kanbanDao.findMemberByName(name);
    }

    @Override
    public Note findNoteById(int id) {
        return null;
    }

    @Override
    public Project findProjectById(int id) {
        return kanbanDao.findProjectById(id);
    }

    @Override
    public Double findProjectProgressById(int id) {
        return kanbanDao.findProjectProgressById(id);
    }

    @Override
    public Status findStatusByName(String name) {
        return kanbanDao.findStatusByName(name);
    }

    @Override
    public Task findTaskById(int id) {
        return kanbanDao.findTaskById(id);
    }

    @Override
    public void save(Announcement announcement) {
        kanbanDao.save(announcement);
    }

    @Override
    public void save(File file) {
        kanbanDao.save(file);
    }

    @Override
    public void save(Member member) {
        kanbanDao.save(member);
    }

    @Override
    public void save(Note note) {
        kanbanDao.save(note);
    }

    @Override
    public void save(Project project) {
        kanbanDao.save(project);
    }

    @Override
    public void save(Task task) {
        kanbanDao.save(task);
    }

    @Override
    public void update(Project project) {
        kanbanDao.update(project);
    }

    @Override
    public void update(Task task) {
        kanbanDao.update(task);
    }
}
