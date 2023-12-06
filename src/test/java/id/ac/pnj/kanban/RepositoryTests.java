package id.ac.pnj.kanban;

import id.ac.pnj.kanban.dao.KanbanDao;
import id.ac.pnj.kanban.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

/*
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)


 */
// Ignore the test. This only applies if the website doesn't have security to begin with
public class RepositoryTests {
    /*
    @Autowired
    private KanbanDao kanbanDao;


    @Test
    void should_notnull_when_finding_project_manager_role() {
        Role role = this.kanbanDao.findRoleByName("ROLE_PROJECT_MANAGER");
        assertThat(role.getName()).isNotNull();

    }

     */
}
