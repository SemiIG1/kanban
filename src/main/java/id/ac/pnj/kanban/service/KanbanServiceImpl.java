package id.ac.pnj.kanban.service;

import id.ac.pnj.kanban.dao.KanbanDao;
import id.ac.pnj.kanban.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KanbanServiceImpl implements KanbanService {
    private KanbanDao kanbanDao;

    public KanbanServiceImpl(KanbanDao kanbanDao) {
        this.kanbanDao = kanbanDao;
    }

    @Override
    public List<Announcement> findAllAnnouncements() {
        return kanbanDao.findAllAnnouncements();
    }

    @Override
    public List<File> findAllFilesByProjectId(int id) {
        return kanbanDao.findAllFilesByProjectId(id);
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
    public List<Note> findAllNotesByProjectId(int id) {
        return kanbanDao.findAllNotesByProjectId(id);
    }

    @Override
    public List<Project> findAllProjects() {
        return kanbanDao.findAllProjects();
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
    public List<Role> findAllRoles() {
        return kanbanDao.findAllRoles();
    }

    @Override
    public Project findProjectById(int id) {
        return kanbanDao.findProjectById(id);
    }

    @Override
    public Status findStatusByName(String name) {
        return kanbanDao.findStatusByName(name);
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
