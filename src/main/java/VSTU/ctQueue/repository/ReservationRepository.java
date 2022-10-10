package VSTU.ctQueue.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import VSTU.ctQueue.entity.AbstractEntrant;
import VSTU.ctQueue.entity.Entrant;
import VSTU.ctQueue.entity.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * <a href=
     * "https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation">"Magic
     * function"</a>, ����� {@link Reservation} ��
     * {@link Reservation#reservationDate} (yyyy-MM-dd HH:mm).
     * 
     * @param date ���� ��� ������
     * @return {@link Reservation} ���� {@link Reservation} �
     *         {@link Reservation#reservationDate} ����������, null - � ���������
     *         ������
     */
    Reservation findByReservationDate(Date date);

    /**
     * ����� {@link Reservation} �� ������������ ���� (��� ����� �������).
     * 
     * @param date ���� ��� ������ (yyyy-MM-dd)
     * @return ������ {@link Reservation} � �������� �����
     */
    @Query("Select r FROM Reservation r WHERE Date(r.reservationDate) = :date ORDER BY r.reservationDate")
    List<Reservation> findByDate(Date date);

    /**
     * ����� {@link Entrant} ������������������ �� ������������ ���� (��� �����
     * �������).
     * 
     * @param date ���� ��� ������ (yyyy-MM-dd)
     * @return ������ {@link Entrant} � �������� �����
     */
    @Query("SELECT r.entrants FROM Reservation r WHERE DATE(r.reservationDate)=:date")
    List<Entrant> findEntrantsByDate(Date date);

    /**
     * �������� {@link Reservation} �� �������� ���� (��� ����� �������).
     * 
     * @param date ���� ��� ������
     */
    @Query("DELETE FROM Reservation r WHERE DATE(r.reservationDate)=:date")
    @Modifying
    @Transactional
    void deleteByDate(Date date);

    /**
     * ��������� ������ ��� ��� ����� �������. ���������� ���� (������ �������).
     * 
     * @return ������ {@link java.util.Date} (yyyy-MM-dd)
     */
    @Query("SELECT DISTINCT DATE(r.reservationDate) FROM Reservation r WHERE r.reservationDate > NOW()")
    List<Date> getCurrUniqDates();

    /**
     * ��������� {@link Reservation#id} � {@link Reservation#reservationDate}
     * (hh:mm:ss) �� �������� ���� (yyyy-MM-dd).
     * 
     * @param date ���� ��� ������
     * @return ������ �������� � ������ ���� Long (id) � Date (hh:mm:ss)
     */
    @Query("SELECT  r.id, TIME(r.reservationDate) FROM Reservation r WHERE DATE(r.reservationDate) = :date ORDER BY r.reservationDate")
    List<Object> getTimes(Date date);

    /**
     * ��������� ������ ��� (������ ������� ����) �� ������� ����������������
     * �����������.
     * 
     * @return ������ {@link Date} (yyyy-MM-dd)
     */
    @Query("SELECT DISTINCT DATE(r.reservationDate) FROM Reservation r WHERE r.reservationDate > NOW() AND size(r.entrants) > 0")
    List<Date> getCurrReserveUniqDates();

    /**
     * ��������� ������� {@link Reservation#id} �
     * {@link Reservation#reservationDate} (hh:mm:ss) �� �������� ���� (yyyy-MM-dd),
     * ��� �������, ��� �� ������ ���� ���������������� ����������(�).
     * 
     * @param date ���� ��� ������
     * @return ������ �������� � ������ ���� Long (id) � Date (hh:mm:ss)
     */
    @Query("SELECT  r.id, TIME(r.reservationDate) FROM Reservation r WHERE size(r.entrants) > 0 AND r.reservationDate > NOW() AND DATE(r.reservationDate) = :date ORDER BY r.reservationDate")
    List<Object> getReserveTimes(Date date);

    /**
     * @param date         - search is around this date
     * @param timeInterval - time interval for searching around date (in seconds)
     * @return ������ {@link Reservation}
     */
    @Deprecated
    @Query("SELECT r FROM Reservation r WHERE TO_SECONDS(:date)+:timeInterval>TO_SECONDS(r.reservationDate) AND TO_SECONDS(:date)-:timeInterval<TO_SECONDS(r.reservationDate)")
    List<Reservation> findTimeAround(Date date, int timeInterval);

    /**
     * ��������� {@link Entrant} �� {@link Reservation} �������� ����
     * ({@link AbstractEntrant#TYPE_MAIN},{@link AbstractEntrant#TYPE_RESERVE}).
     * 
     * 
     * @param reservation {@link Reservation} � ������� ����������� �����
     *                    {@link Entrant}
     * @param typeId      ��� ��� ������.
     *                    ({@link AbstractEntrant#TYPE_MAIN},{@link AbstractEntrant#TYPE_RESERVE}).
     * @return ������ {@link} Entrant}
     */
    @Query("Select e FROM Entrant e WHERE e.reservation=:reservation AND e.type.id=:typeId")
    List<Entrant> findByReservationAndType(Reservation reservation, Long typeId);

}
