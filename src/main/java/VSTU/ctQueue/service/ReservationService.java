package VSTU.ctQueue.service;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import VSTU.ctQueue.entity.Entrant;
import VSTU.ctQueue.entity.Reservation;
import VSTU.ctQueue.model.ReservationGenerateModel;
import VSTU.ctQueue.repository.ReservationRepository;
import lombok.Getter;


@Service
public class ReservationService extends CrudImpl<Reservation> {

    /**
     * TIMETABLE_MAIN = 1l
     */
    public final static long TIMETABLE_MAIN = 1l;

    /**
     * TIMETABLE_RESERVE = 2l
     */
    public final static long TIMETABLE_RESERVE = 2l;

    private ReservationRepository reservationRepository;

    @Deprecated
    @Value("${timetable.registration.time.interval}")
    private int timeInterval;

    /**
     * ����� �������� ����������
     */
    @Value("${operator.main.count}")
    private int maxMainCount;

    /**
     * ����� ��������� ����������
     */
    @Value("${operator.reserve.count}")
    private int maxReserveCount;

    @Value("${java.util.Date.timeZone}")
    private String timeZone;

    @Autowired
    public ReservationService(ReservationRepository repository, ReservationRepository reservationService) {
        super(repository);
        this.reservationRepository = reservationService;
    }

    /**
     * �������� {@link Reservation} � �������������� �� ������ � ��.
     * 
     * @param date ���� (yyyy-MM-dd HH:mm:ss) �������� {@link Reservation}
     * @throws Exception
     */
    public void create(final Date date) throws Exception {
        create(new Reservation(date));
    }

    /**
     * ����� {@link Reservation} �� {@link Reservation#reservationDate} (yyyy-MM-dd
     * HH:mm:ss).
     * 
     * @param date ���� ��� ������
     * @return {@link Reservation} ���� {@link Reservation} �
     *         {@link Reservation#reservationDate} ����������, null - � ���������
     *         ������
     */
    public Reservation findByTimestamp(final Date date) {
        return reservationRepository.findByReservationDate(date);
    }

    // True if there is no time around
    // *60 - convert seconds in minuts
    @Deprecated
    public boolean timeAroundCheck(final Reservation reservation) {
        return reservationRepository.findTimeAround(reservation.getReservationDate(), timeInterval * 60).isEmpty();
    }

    /**
     * ��������, ���� �� ��������� ����� �� ����������� � �������� �����
     * 
     * @param reservation ����������� {@link Reservation}
     * @return TRUE - ���� ����, FALSE - ���� ���
     */
    public Boolean chekMainCounter(final Reservation reservation) {
        Reservation currReservation = findByTimestamp(reservation.getReservationDate());
        return (currReservation == null || reservationRepository
                .findByReservationAndType(currReservation, Entrant.TYPE_MAIN).size() < maxMainCount) ? Boolean.TRUE
                        : Boolean.FALSE;
    }

    /**
     * ��������, ���� �� ��������� ����� �� ����������� � ��������� �����
     * 
     * @param reservation ����������� {@link Reservation}
     * @return TRUE - ���� ����, FALSE - ���� ���
     */
    public Boolean chekReserveCounter(final Reservation reservation) {
        Reservation currReservation = findByTimestamp(reservation.getReservationDate());
        return (currReservation == null || reservationRepository
                .findByReservationAndType(currReservation, Entrant.TYPE_RESERVE).size() < maxReserveCount)
                        ? Boolean.TRUE
                        : Boolean.FALSE;
    }

    /**
     * ��������� ���������� ������� ���� � �������� �����.
     * 
     * @param reservation ����������� {@link Reservation}
     * @return ���������� ������� ���� � �������� �����
     */
    public Integer getMainCounter(final Reservation reservation) {
        Reservation currReservation = findByTimestamp(reservation.getReservationDate());
        return currReservation != null
                ? reservationRepository.findByReservationAndType(currReservation, Entrant.TYPE_MAIN).size()
                : 0;

    }

    /**
     * ��������� ���������� ������� ���� � ��������� �����.
     * 
     * @param reservation ����������� {@link Reservation}
     * @return ���������� ������� ���� � ��������� �����
     */
    public Integer getReserveCounter(final Reservation reservation) {
        Reservation currReservation = findByTimestamp(reservation.getReservationDate());
        return currReservation != null
                ? reservationRepository.findByReservationAndType(currReservation, Entrant.TYPE_RESERVE).size()
                : 0;

    }

    /**
     * �������� ��������� ���.
     * 
     * @param generateModel ������ ��� ��������� ����� {@link Reservation}
     * @return TRUE - ���� � {@link ReservationGenerateModel} ����� ���� ���
     *         ��������� {@link Reservation} �� ���������� � ��, FALSE - � ���������
     *         ������
     * @throws ParseException
     * @throws Exception
     */
    public Boolean checkTimetable(final ReservationGenerateModel generateModel) throws ParseException, Exception {
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFromat = new SimpleDateFormat("HH:mm");
        timeFromat.setTimeZone(TimeZone.getTimeZone(timeZone));
        Date startDay = dayFormat.parse(generateModel.getStartDate());
        Date endDay = dayFormat.parse(generateModel.getEndDate());
        Date startTime = timeFromat.parse(generateModel.getStartTime());
        Date endTime = timeFromat.parse(generateModel.getEndTime());
        while (startDay.getTime() <= endDay.getTime()) {
            while (startTime.getTime() <= endTime.getTime()) {
                if (findByTimestamp(new Date(startTime.getTime() + startDay.getTime())) != null)
                    return Boolean.FALSE;
                startTime.setTime(startTime.getTime() + generateModel.getInterval() * 1000 * 60);
            }
            startTime.setTime(timeFromat.parse(generateModel.getStartTime()).getTime());
            startDay.setTime(startDay.getTime() + 24 * 1000 * 60 * 60);
        }
        return Boolean.TRUE;
    }

    /**
     * ��������� ����� {@link Reservation} �������� ������
     * {@link ReservationGenerateModel}
     * 
     * @param generateModel ������ ��� ��������� {@link Reservation}
     * @throws ParseException
     * @throws Exception
     */
    public void generateTimetable(final ReservationGenerateModel generateModel) throws ParseException, Exception {
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFromat = new SimpleDateFormat("HH:mm");
        timeFromat.setTimeZone(TimeZone.getTimeZone(timeZone));
        Date startDay = dayFormat.parse(generateModel.getStartDate());
        Date endDay = dayFormat.parse(generateModel.getEndDate());
        Date startTime = timeFromat.parse(generateModel.getStartTime());
        Date endTime = timeFromat.parse(generateModel.getEndTime());
        while (startDay.getTime() <= endDay.getTime()) {
            while (startTime.getTime() <= endTime.getTime()) {
                create(new Date(startTime.getTime() + startDay.getTime()));
                startTime.setTime(startTime.getTime() + generateModel.getInterval() * 1000 * 60);
            }
            startTime.setTime(timeFromat.parse(generateModel.getStartTime()).getTime());
            startDay.setTime(startDay.getTime() + 24 * 1000 * 60 * 60);
        }
    }

    /**
     * ��������� ���������� �������� � ��������� ����������.
     * 
     * @return ������ � ������ ���� Integer (����� �������� ����������, �����
     *         ��������� ����������)
     */
    public Object getCounters() {
        return new Object() {
            @Getter
            private int maxMainCounter = ReservationService.this.maxMainCount;
            @Getter
            private int maxReserveCount = ReservationService.this.maxReserveCount;
        };
    }

    /**
     * �������� {@link Reservation} �� �������� ���� (��� ����� �������).
     * 
     * @param date ���� ��� ������ (yyyy-MM-dd)
     */
    public void deleteByDate(Date date) throws Exception {
        reservationRepository.deleteByDate(date);
    }

    /**
     * �������� ����������� �������� {@link Reservation}.
     * 
     * @param date ���� {@link Reservation} ��� ������� ������������ ��������
     *             (yyyy-MM-dd)
     * @return TRUE - ���� �� ������ {@link Reservation} ����� �� ��������������� �
     *         �������� ��������, FALSE - � ��������� �����
     */
    public Boolean checkRemovable(Date date) {
        return reservationRepository.findEntrantsByDate(date).size() == 0;

    }

    /**
     * ����� {@link Reservation} �� ������������ ���� (��� ����� �������).
     * 
     * @param date ���� ��� ������ (yyyy-MM-dd)
     * @return ������ {@link Reservation} � �������� �����
     */
    public List<Reservation> findByDate(final Date date) {
        return reservationRepository.findByDate(date);
    }

    /**
     * ��������� ������ ���� {@link Reservation} (���� ���� ����� ��� �����������)
     * �������� ����.
     * 
     * @param timetableType ��� {@link Reservation}
     *                      ({@link ReservationService#TIMETABLE_MAIN},
     *                      {@link ReservationService#TIMETABLE_RESERVE})
     * @return ���������� ������ �������� � ���������� ������: {@link Date} (����,
     *         yyyy-MM-dd), {@link Boolean} (����� �� ������� ������
     *         {@link Reservation}), ������ {@link Date} (�����, ��������� ���
     *         ����������� � ������ ����, HH:mm:ss)
     * @throws ParseException
     */
    public List<Object> getTimetable(final Long timetableType) throws ParseException {
        List<Object> objs = new ArrayList<>();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        timeFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        for (Date currDate : reservationRepository.getCurrUniqDates()) {
            objs.add(new Object() {
                @Getter
                private Date date = currDate;
                @Getter
                private List<Object> times = new ArrayList<>();

                @Getter
                private Boolean removable = false;

                {
                    removable = checkRemovable(currDate);
                    for (Object obj : reservationRepository.getTimes(currDate)) {
                        if (timetableType == ReservationService.TIMETABLE_MAIN)
                            if (chekMainCounter(read((Long) Array.get(obj, 0))) && currDate.getTime() + timeFormat
                                    .parse(String.valueOf(Array.get(obj, 1))).getTime() > new Date().getTime())
                                times.add(obj);
                        if (timetableType == ReservationService.TIMETABLE_RESERVE)
                            if (chekReserveCounter(read((Long) Array.get(obj, 0))) && currDate.getTime() + timeFormat
                                    .parse(String.valueOf(Array.get(obj, 1))).getTime() > new Date().getTime())
                                times.add(obj);
                    }
                }
            });
        }
        return objs;
    }

    /**
     * ��������� ������ ���� {@link Reservation} �� ������� ���� �����������.
     * 
     * @return ���������� ������ �������� � ���������� ������: {@link Date} (����,
     *         yyyy-MM-dd), ������ �������� � ���������� ������:{@link Long}(id
     *         {@link Reservation}), {@link Date} (�����, ������� � ������ ����,
     *         HH:mm:ss), {@link Integer} (���������� ������� ���� � ��������
     *         �����), {@link Integer} (���������� ������� ���� � ��������� �����)
     * 
     */
    public List<Object> getReverseTimetable() {
        List<Object> objs = new ArrayList<>();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        timeFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        for (Date currDate : reservationRepository.getCurrReserveUniqDates()) {
            objs.add(new Object() {
                @Getter
                private Date date = currDate;
                @Getter
                private List<Object> times = new ArrayList<>();
                {
                    for (Object obj : reservationRepository.getReserveTimes(currDate)) {
                        Object[] array = new Object[4];
                        array[0] = Array.get(obj, 0);
                        array[1] = Array.get(obj, 1);
                        array[2] = getMainCounter(read((Long) Array.get(obj, 0)));
                        array[3] = getReserveCounter(read((Long) Array.get(obj, 0)));
                        times.add(array);
                    }
                }
            });
        }
        return objs;
    }

    /**
     * �������� {@link Reservation#reservationDate}
     * 
     * @param id {@link Reservation#id}
     * @return TRUE - ���� {@link Reservation#reservationDate} ������ ������� ����,
     *         FALSE - � ��������� ������
     */
    public Boolean reservationIsValid(final Long id) {
        return read(id).getReservationDate().getTime() > new Date().getTime();
    }
}
