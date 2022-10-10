package VSTU.ctQueue.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import VSTU.ctQueue.entity.Entrant;


@Repository
public interface EntrantRepository extends JpaRepository<Entrant, Long> {

    /**
     * <a href=
     * "https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation">"Magic
     * function"</a>
     * 
     * @param email ����������� ����� ��� ������
     * @return {@link Entrant} ���� ���������� � ������ ������� ����������� �����
     *         ����������, null - � ��������� ������
     */
    Entrant findByEmail(final String email);

    /**
     * <a href=
     * "https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation">"Magic
     * function"</a>
     * 
     * @param phone ����� �������� ��� ������
     * @return {@link Entrant} ���� ���������� � ������ ������� �������� ����������,
     *         null - � ��������� ������
     */
    Entrant findByPhone(final Long phone);

    /**
     * <a href=
     * "https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation">"Magic
     * function"</a>
     * 
     * @param activationCode ��� ��������� ��� ������
     * @return {@link Entrant} ���� ���������� � ������ ����� ��������� ����������,
     *         null - � ��������� ������
     */
    Entrant findByActivationCode(final String activationCode);

    /**
     * �������� ���� �������������, �� ������������� ����� ����������� ����� �
     * ������������������ ����� ���� �����
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Entrant e WHERE TIMEDIFF(NOW(), e.regDate) > '01:00:00' AND e.activationCode IS NOT NULL")
    void deleteUnapproveExpired();

    /**
     * ��������� ������� {@link Entrant}, ��� ����� ���������� ��� ������
     * 
     * @return ������ {@link Entrant}
     */
    @Query("SELECT e FROM Entrant e WHERE e.reservation.reservationDate <= NOW() AND e.activationCode IS NULL")
    List<Entrant> getEntrantsToTransfer();

    /**
     * <a href=
     * "https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation">"Magic
     * function"</a>
     * 
     * @param surname ������� ��� ������
     * @return {@link Entrant} ���� ���������� � ������ �������� ����������, null -
     *         � ��������� ������
     */
    List<Entrant> findBySurnameContainingIgnoreCase(String surname);
}
