package id.ac.pnj.kanban.service;

import id.ac.pnj.kanban.entity.Member;
import id.ac.pnj.kanban.user.WebUser;
import org.springframework.security.core.userdetails.UserDetailsService;

// User is referred to member
public interface UserService extends UserDetailsService {
    Member findUserByEmail(String email);

    void save(WebUser webUser);
}
