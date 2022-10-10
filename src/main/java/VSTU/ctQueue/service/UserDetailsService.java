package VSTU.ctQueue.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import VSTU.ctQueue.entity.Role;
import VSTU.ctQueue.entity.User;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userService.findByUsername(username);
        UserDetails userDetails = null;
        List<GrantedAuthority> grantList = null;
        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        } else if (user.getRoleList() != null) {
            grantList = new ArrayList<GrantedAuthority>();
            for (Role role : user.getRoleList()) {
                GrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
                grantList.add(authority);
            }
            userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                    grantList);
        }
        return userDetails;
    }
}
