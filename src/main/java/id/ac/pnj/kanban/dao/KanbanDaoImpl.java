package id.ac.pnj.kanban.dao;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import id.ac.pnj.kanban.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class KanbanDaoImpl implements KanbanDao {
    @PersistenceContext
    private EntityManager entityManager;

    private CriteriaBuilderFactory criteriaBuilderFactory;
    public KanbanDaoImpl(EntityManager entityManager, CriteriaBuilderFactory criteriaBuilderFactory) {
        this.entityManager = entityManager;
        this.criteriaBuilderFactory = criteriaBuilderFactory;
    }

    @Override
    public PagedList<Announcement> findAllAnnouncements(Pageable pageable) {
        return criteriaBuilderFactory
                .create(entityManager, Announcement.class)
                .fetch("member")
                .orderByAsc("id")
                .page((int) pageable.getOffset(), pageable.getPageSize())
                .getResultList();

    }

    @Override
    public Announcement findAnnouncementById(int id) {
        TypedQuery<Announcement> query =
                entityManager.createQuery("from Announcement where id = :id", Announcement.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }


    @Override
    public List<Task> findAllDoneTasksByProjectId(int id) {
        return criteriaBuilderFactory.create(entityManager, Task.class)
                .fetch("member")
                .fetch("project")
                .fetch("status")
                .whereOr().whereAnd()
                .where("project.id").eq(id)
                .where("status.id").eq(3)
                .endAnd()
                .endOr()
                .orderByAsc("id")
                .getResultList();
    }

    @Override
    public List<File> findAllFilesByProjectId(int id) {
        TypedQuery<File> query = entityManager.createQuery("select f from File f " +
                "join fetch f.project p " +
                "join fetch f.member m " +
                "where p.id = :projectId", File.class);
        query.setParameter("projectId", id);
        criteriaBuilderFactory.create(entityManager, File.class)
                .fetch("project")
                .fetch("member")
                .whereOr()
                .where("project.id").eq(id)
                .endOr()
                .orderByAsc("id")
                .page( 0, 20)
                .getResultList();
        return query.getResultList();
    }

    @Override
    public List<Task> findAllInProgressTasksByProjectId(int id) {
        return criteriaBuilderFactory.create(entityManager, Task.class)
                .fetch("member")
                .fetch("project")
                .fetch("status")
                .whereOr()
                .whereAnd()
                .where("project.id").eq(id)
                .where("status.id").eq(2)
                .endAnd()
                .endOr()
                .orderByAsc("id")
                .getResultList();
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
        TypedQuery<Member> query = entityManager.createQuery("select member from Member member " +
                "where member not in (select m from Member m join m.projects p where p.id = :projectId)", Member.class);
        /*
        entityManager.createQuery("select m from Member m " +
                "join m.projects p where p.id = :id", Member.class);

         */
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
    public File findFileById(int id) {
        TypedQuery<File> query =
                entityManager.createQuery("from File f where f.id = :id", File.class);
        query.setParameter("id", id);
        return query.getSingleResult();
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
    public Member findMemberByName(String name) {
        TypedQuery<Member> query = entityManager.createQuery("from Member where name = :name", Member.class);
        query.setParameter("name", name);
        Member member = null;
        try {
            member = query.getSingleResult();
        } catch (Exception e) {

        }
        return member;
    }

    @Override
    public Note findNoteById(int id) {
        TypedQuery<Note> query =
                entityManager.createQuery("from Note n where n.id = :id", Note.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public PagedList<Note> findAllNotesByProjectId(int id, Pageable pageable) {

        return criteriaBuilderFactory.create(entityManager, Note.class)
                .fetch("project")
                .fetch("member")
                .whereOr()
                .where("project.id").eq(id)
                .endOr()
                .orderByAsc("id")
                .page((int) pageable.getOffset(), pageable.getPageSize())
                .getResultList();
    }

    @Override
    public PagedList<Project> findAllProjects(Pageable pageable) {
        return criteriaBuilderFactory.create(entityManager, Project.class)
                .fetch("status")
                .orderByAsc("id")
                .page((int) pageable.getOffset(), pageable.getPageSize())
                .getResultList();
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
        return criteriaBuilderFactory.create(entityManager, Task.class)
                .fetch("member")
                .fetch("project")
                .fetch("status")
                .whereOr()
                .where("project.id").eq(id)
                .endOr()
                .orderByAsc("id")
                .getResultList();
    }

    @Override
    public PagedList<Task> findAllTasksByProjectId(int id, Pageable pageable) {
        return criteriaBuilderFactory.create(entityManager, Task.class)
                .fetch("member")
                .fetch("project")
                .fetch("status")
                .whereOr()
                .where("project.id").eq(id)
                .endOr()
                .orderByAsc("id")
                .page((int) pageable.getOffset(), pageable.getPageSize())
                .getResultList();
    }

    @Override
    public List<Task> findAllToDoTasksByProjectId(int id) {
        return criteriaBuilderFactory.create(entityManager, Task.class)
                .fetch("member")
                .fetch("project")
                .fetch("status")
                .whereOr().whereAnd()
                .where("project.id").eq(id)
                .where("status.id").eq(1)
                .endAnd()
                .endOr()
                .orderByAsc("id")
                .getResultList();
    }

    @Override
    public Project findProjectById(int id) {
        TypedQuery<Project> query = entityManager.createQuery("from Project p where p.id = :id", Project.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public Double findProjectProgressById(int id) {
        List<Object[]> results = entityManager
                .createQuery("select count(*), t.status from Task t " +
                        "join fetch Project p where p.id = :id group by t.status", Object[].class)
                .setParameter("id", id).getResultList();
        int doneTasks = 0;
        int totalTasks = 0;

        for (var result: results) {
            int count = (int) result[0];
            totalTasks += count;
            Status status = (Status) result[1];
            if (Objects.equals(status.getName(), "Done")) {
                doneTasks += count;
            }
        }
        Double percentage = (double) doneTasks / totalTasks;
        return percentage;
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
    public Task findTaskById(int id) {
        TypedQuery<Task> theQuery = entityManager.createQuery("from Task where id=:id", Task.class);
        theQuery.setParameter("id", id);
        Task task = null;
        try {
            task = theQuery.getSingleResult();
        } catch (Exception e) {
            task = null;
        }
        return task;
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
