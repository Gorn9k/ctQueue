package VSTU.ctQueue.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import VSTU.ctQueue.entity.User;
import VSTU.ctQueue.repository.UserRepository;

@Service
public class UserService extends CrudImpl<User> {

    private final static Long ROLE_OPERATOR = 2l;

    private UserRepository userRepository;

    private RoleService roleService;

    private BCryptPasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository repository, RoleService roleService, BCryptPasswordEncoder encoder) {
        super(repository);
        this.userRepository = repository;
        this.roleService = roleService;
        this.encoder = encoder;
    }

    /**
     * 
     * @param username ��� ������������ ��� ������
     * @return {@link User} ���� ������������ � ������ ������ ����������, null - �
     *         ��������� ������
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * ����������� ������������. (���������)
     *
     * @param user {@link User} ��� �����������.
     * @return {@link Boolean}
     * @throws Exception
     */
    public Boolean register(final User user) throws Exception {
        user.setRoleList(Arrays.asList(roleService.read(ROLE_OPERATOR)));
        user.setPassword(encoder.encode(user.getPassword()));
        create(user);
        return Boolean.TRUE;
    }

}
