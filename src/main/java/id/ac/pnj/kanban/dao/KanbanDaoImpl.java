package id.ac.pnj.kanban.dao;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import id.ac.pnj.kanban.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;


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
    @Transactional
    public void deleteAnnouncementById(int id) {
        Announcement announcement = entityManager.find(Announcement.class, id);
        entityManager.remove(announcement);
    }

    @Override
    @Transactional
    public void deleteEventById(int id) {
        Event event = entityManager.find(Event.class, id);
        entityManager.remove(event);
    }

    @Override
    @Transactional
    public void deleteFileById(int id) {
        File file = entityManager.find(File.class, id);
        entityManager.remove(file);
    }

    @Override
    @Transactional
    public void deleteProjectById(int id) {
        Project project = entityManager.find(Project.class, id);
        entityManager.remove(project);
    }

    @Override
    @Transactional
    public void deleteProjectMemberById(int projectId, int memberId) {
        entityManager
                .createNativeQuery("DELETE FROM project_member " +
                        "WHERE project_id = :projectId AND member_id = :memberId")
                .setParameter("projectId", projectId)
                .setParameter("memberId", memberId).executeUpdate();
    }

    @Override
    @Transactional
    public void deleteNoteById(int id) {
        Note note = entityManager.find(Note.class, id);
        entityManager.remove(note);
    }

    @Override
    @Transactional
    public void deleteTaskById(int id) {
        Task task = entityManager.find(Task.class, id);
        entityManager.remove(task);
    }

    @Override
    public PagedList<Announcement> findAllAnnouncements(Pageable pageable) {
        return criteriaBuilderFactory
                .create(entityManager, Announcement.class)
                .fetch("createdBy")
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
                .fetch("createdBy")
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
    public List<Event> findAllEvents() {
        TypedQuery<Event> query = entityManager.createQuery("from Event", Event.class);
        return query.getResultList();
    }

    @Override
    public List<File> findAllFilesByProjectId(int id) {

        return criteriaBuilderFactory.create(entityManager, File.class)
                .fetch("project")
                .fetch("member")
                .whereOr()
                .where("project.id").eq(id)
                .endOr()
                .orderByAsc("id")
                .page( 0, 20)
                .getResultList();
    }

    @Override
    public List<Task> findAllInProgressTasksByProjectId(int id) {
        return criteriaBuilderFactory.create(entityManager, Task.class)
                .fetch("createdBy")
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
    public List<Object[]> findAllMembersByProjectId(int id) {

        List<Object[]> membersInProject = entityManager
                .createNativeQuery("SELECT * FROM member WHERE member.id IN " +
                        "(SELECT member_id FROM project_member WHERE project_id = :id)")
                .setParameter("id", id)
                .getResultList();

        return membersInProject;
    }

    @Override
    public List<Object[]> findAllMembersNotInProjectWithProjectId(int id) {

        List<Object[]> membersNotInProject = entityManager
                .createNativeQuery("SELECT * FROM member WHERE member.id NOT IN " +
                "(SELECT member_id FROM project_member WHERE project_id = :id)")
                .setParameter("id", id)
                .getResultList();



        return membersNotInProject;
    }

    @Override
    public List<Member> findAllMembers() {
        TypedQuery<Member> query = entityManager.createQuery("select m from Member m " +
                "join fetch m.roles r", Member.class);
        return query.getResultList();
    }

    @Override
    public Event findEventById(int id) {
        Event event = entityManager.find(Event.class, id);
        return event;
    }

    @Override
    public File findFileById(int id) {
        TypedQuery<File> query =
                entityManager.createQuery("from File f where f.id = :id", File.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public List<Task> findAllTasksOrderedByUpdatedAtDesc() {
        return entityManager.createQuery("select t from Task t " +
                "join t.project p " +
                "join t.status s " +
                "order by t.updatedAt desc").getResultList();
    }

    @Override
    public Member findMemberById(int id) {
        return entityManager.find(Member.class, id);
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
                .fetch("createdBy")
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
                .fetch("createdBy")
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
                .fetch("createdBy")
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
                .fetch("createdBy")
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
    public int findProjectProgressById(int id) {
        List<Object[]> results = entityManager
                .createNativeQuery("SELECT status_id, COUNT(*) FROM task " +
                        "WHERE project_id = :id GROUP BY status_id", Object[].class)
                .setParameter("id", id).getResultList();
        int doneTasks = 0;
        int inProgressTasks = 0;
        int totalTasks = 0;

        for (var result: results) {
            int statusId = (int) result[0];
            long count = (long) result[1];
            totalTasks += count;
            if (statusId == 2) {
                inProgressTasks += count;
            }
            if (statusId == 3) {
                doneTasks += count;
            }
        }
        Double doublePercentage = (( 0.5 * (double) inProgressTasks + (double) doneTasks) / totalTasks) * 100;
        return doublePercentage.intValue();
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
        Announcement databaseAnnouncement = entityManager.find(Announcement.class, announcement.getId());
        if (databaseAnnouncement == null) {
            entityManager.persist(announcement);
        } else {
            entityManager.merge(announcement);
        }

    }

    @Override
    @Transactional
    public void save(Event event) {
        entityManager.persist(event);
    }

    @Override
    @Transactional
    public void save(File file) {
        entityManager.persist(file);
    }

    @Override
    @Transactional
    public void save(Member member) {
        Member databaseMember = entityManager.find(Member.class, member.getId());
        if (databaseMember == null) {
            entityManager.persist(member);
        } else {
            entityManager.merge(member);
        }
    }

    @Override
    @Transactional
    public void save(Note note) {
        Note databaseNote = entityManager.find(Note.class, note.getId());
        if (databaseNote == null) {
            entityManager.persist(note);
        } else {
            entityManager.merge(note);
        }
    }

    @Override
    @Transactional
    public void save(Project project) {

        Project databaseProject = entityManager.find(Project.class, project.getId());
        if (databaseProject == null) {
            entityManager.persist(project);
        } else {
            entityManager.merge(project);
        }

    }

    @Override
    @Transactional
    public void save(Task task) {
        Task databaseTask = entityManager.find(Task.class, task.getId());
        if (databaseTask == null) {
            entityManager.persist(task);
        } else {
            entityManager.merge(task);
        }
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
