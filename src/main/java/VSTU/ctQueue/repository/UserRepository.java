package VSTU.ctQueue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import VSTU.ctQueue.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * <a href=
     * "https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation">"Magic
     * function"</a>
     * 
     * @param username ��� ������������ ��� ������
     * @return {@link User} ���� ������������ � ������ ������ ����������, null - �
     *         ��������� ������
     */
    User findByUsername(String username);

}
