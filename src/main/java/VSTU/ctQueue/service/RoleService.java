package VSTU.ctQueue.service;

import org.springframework.stereotype.Service;

import VSTU.ctQueue.entity.Role;
import VSTU.ctQueue.repository.RoleRepository;

@Service
public class RoleService extends CrudImpl<Role> {

    public RoleService(RoleRepository repository) {
        super(repository);
    }

}
