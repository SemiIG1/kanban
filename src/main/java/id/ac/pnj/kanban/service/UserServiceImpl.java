package id.ac.pnj.kanban.service;

import id.ac.pnj.kanban.dao.KanbanDao;
import id.ac.pnj.kanban.entity.Member;
import id.ac.pnj.kanban.entity.Role;
import id.ac.pnj.kanban.user.WebUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private KanbanDao kanbanDao;

    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public UserServiceImpl(KanbanDao kanbanDao, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.kanbanDao = kanbanDao;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Member findUserByEmail(String email) {
        return kanbanDao.findMemberByEmail(email);
    }

    @Override
    public void save(WebUser webUser) {
        Member member = new Member();

        member.setName(webUser.getName());
        member.setEmail(webUser.getEmail());
        member.setPassword(bCryptPasswordEncoder.encode(webUser.getPassword()));
        member.setPhone(webUser.getPhone());
        // member.setEnabled(1);
        System.out.println(webUser.getRole());

        member.addRole(kanbanDao.findRoleByName(webUser.getRole()));
        kanbanDao.save(member);

    }

    // Username is referred to email
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = kanbanDao.findMemberByEmail(username);
        if (member == null) {
            throw new UsernameNotFoundException("Invalid email or password");
        }
        return new org.springframework.security.core.userdetails.User
                (member.getEmail(), member.getPassword(), mapRolesToAuthorities(member.getRoles())
                );
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(
                role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()
        );
    }
}
