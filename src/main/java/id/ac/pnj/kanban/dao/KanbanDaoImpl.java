package id.ac.pnj.kanban.dao;

import id.ac.pnj.kanban.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class KanbanDaoImpl implements KanbanDao{
    private EntityManager entityManager;

    public KanbanDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Announcement> findAllAnnouncements() {
        TypedQuery<Announcement> query = entityManager.createQuery("select a from Announcement a " +
                "join fetch a.member m", Announcement.class);
        return query.getResultList();
    }

    @Override
    public List<File> findAllFilesByProjectId(int id) {
        TypedQuery<File> query = entityManager.createQuery("select f from File f " +
                "join fetch f.project p " +
                "join fetch f.member m " +
                "where p.id = :projectId", File.class);
        query.setParameter("projectId", id);
        return query.getResultList();
    }

    @Override
    public List<Member> findAllMembersByProjectId(int id) {
        TypedQuery<Member> query = entityManager.createQuery("select m from Member m " +
                "join fetch m.projects p " +
                "where p.id = :projectId", Member.class);
        query.setParameter("projectId", id);
        return query.getResultList();
    }

    @Override
    public List<Member> findAllMembersNotInProjectWithProjectId(int id) {
        TypedQuery<Member> query = entityManager.createQuery("select m from Member m " +
                "where :projectId not in (select id from Project)", Member.class);
        query.setParameter("projectId", id);
        return query.getResultList();
    }

    @Override
    public List<Member> findAllMembers() {
        TypedQuery<Member> query = entityManager.createQuery("select m from Member m " +
                "join fetch m.roles r", Member.class);
        return query.getResultList();
    }



    @Override
    public Member findMemberByEmail(String email) {
        TypedQuery<Member> query = entityManager.createQuery("from Member where email = :email", Member.class);
        query.setParameter("email", email);
        Member member = null;
        try {
            member = query.getSingleResult();
        } catch (Exception e) {

        }
        return member;
    }

    @Override
    public List<Note> findAllNotesByProjectId(int id) {
        TypedQuery<Note> query = entityManager.createQuery("select n from Note n " +
                "join fetch n.project p " +
                "join fetch n.member m " +
                "where p.id = :projectId", Note.class);
        query.setParameter("projectId", id);
        return query.getResultList();
    }

    @Override
    public List<Project> findAllProjects() {
        TypedQuery<Project> query = entityManager.createQuery("select p from Project p " +
                "join fetch p.status s", Project.class);
        return query.getResultList();
    }

    @Override
    public List<Role> findAllRoles() {
        TypedQuery<Role> query = entityManager.createQuery("from Role", Role.class);
        return query.getResultList();
    }

    @Override
    public List<Status> findAllStatuses() {
        TypedQuery<Status> query = entityManager.createQuery("from Status", Status.class);
        return query.getResultList();
    }

    @Override
    public List<Task> findAllTasksByProjectId(int id) {
        TypedQuery<Task> query = entityManager.createQuery("select t from Task t " +
                "join fetch t.member m " +
                "join fetch t.project p " +
                "join fetch t.status s " +
                "where p.id = :projectId", Task.class);
        query.setParameter("projectId", id);
        return query.getResultList();
    }

    @Override
    public Project findProjectById(int id) {
        TypedQuery<Project> query = entityManager.createQuery("from Project p where p.id = :id", Project.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public Role findRoleByName(String name) {
        TypedQuery<Role> theQuery = entityManager.createQuery("from Role where name=:roleName", Role.class);
        theQuery.setParameter("roleName", name);

        Role theRole = null;

        try {
            theRole = theQuery.getSingleResult();
        } catch (Exception e) {
            theRole = null;
        }

        return theRole;
    }

    @Override
    public Status findStatusByName(String name) {
        TypedQuery<Status> theQuery = entityManager.createQuery("from Status where name=:name", Status.class);
        theQuery.setParameter("name", name);

        Status theStatus = null;

        try {
            theStatus = theQuery.getSingleResult();
        } catch (Exception e) {
            theStatus = null;
        }

        return theStatus;
    }

    @Override
    @Transactional
    public void save(Announcement announcement) {
        entityManager.persist(announcement);
    }

    @Override
    @Transactional
    public void save(File file) {
        entityManager.persist(file);
    }

    @Override
    @Transactional
    public void save(Member member) {
        entityManager.merge(member);
    }

    @Override
    @Transactional
    public void save(Note note) {
        entityManager.persist(note);
    }

    @Override
    @Transactional
    public void save(Project project) {
        entityManager.persist(project);
    }

    @Override
    @Transactional
    public void save(Task task) {
        entityManager.persist(task);
    }

    @Override
    @Transactional
    public void update(Project project) {
        entityManager.merge(project);
    }

    @Override
    @Transactional
    public void update(Task task) {
        entityManager.merge(task);
    }
}
