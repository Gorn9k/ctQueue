package VSTU.ctQueue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import VSTU.ctQueue.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
