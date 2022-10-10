package VSTU.ctQueue.service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import VSTU.ctQueue.entity.Entrant;
import VSTU.ctQueue.entity.Reservation;
import VSTU.ctQueue.repository.EntrantHistoryRepository;
import VSTU.ctQueue.repository.EntrantRepository;

@Service
public class EntrantService extends CrudImpl<Entrant> {

    private EntrantRepository entrantRepository;

    private EntrantHistoryRepository entrantHistoryRepository;

    private ReservationService reservationService;

    /**
     * ���\���� �������� ����������� ���������
     */
    @Value("${entrant.email.validation}")
    private Boolean emailValidation;

    @Value("${entrant.email.header}")
    private String emailHeader;

    @Value("${entrant.email.body}")
    private String emailBody;

    @Autowired
    private MailSenderService mailSender;

    @Autowired
    public EntrantService(final EntrantRepository repository, final ReservationService reservationService,
            final EntrantHistoryRepository entrantHistoryRepository) {
        super(repository);
        this.entrantRepository = repository;
        this.reservationService = reservationService;
        this.entrantHistoryRepository = entrantHistoryRepository;
    }

    /**
     * @param email ����������� ����� ��� ������
     * @return {@link Entrant} ���� ���������� � ������ ������� ����������� �����
     *         ����������, null - � ��������� ������
     */
    public Entrant findByEmail(String email) {
        return entrantRepository.findByEmail(email);
    }

    /**
     * @param phone ����� �������� ��� ������
     * @return {@link Entrant} ���� ���������� � ������ ������� �������� ����������,
     *         null - � ��������� ������
     */
    public Entrant findByPhone(Long phone) {
        return entrantRepository.findByPhone(phone);
    }

    /**
     * @param activationCode ��� ��������� ��� ������
     * @return {@link Entrant} ���� ���������� � ������ ����� ��������� ����������,
     *         null - � ��������� ������
     */
    public Entrant findByActivationCode(String activationCode) {
        return entrantRepository.findByActivationCode(activationCode);
    }

    /**
     * �������� ���� �������������, �� ������������� ����� ����������� ����� �
     * ������������������ ����� ���� �����
     */
    public void deleteUnapproveExpired() {
        entrantRepository.deleteUnapproveExpired();
    }

    /**
     * ����������� � ������� ������������, ��� ����� ���������� ��� ������
     */
    public void entrantsTransferToHisotry() {
        for (Entrant entrant : entrantRepository.getEntrantsToTransfer()) {
            entrantHistoryRepository.save(entrant.castToEntrantHistory());
            entrantRepository.delete(entrant);
        }
        entrantHistoryRepository.flush();
    }

    /**
     * �������� ������������� ����������� (�� id).
     * 
     * @param id ���������� ������������� ��� ��������
     * @return {@link Boolean}
     */
    public Boolean entrantIsExist(Long id) {
        return null == read(id) ? Boolean.FALSE : Boolean.TRUE;
    }

    /**
     * ������������� ������ ����������� �����.
     * 
     * @param activationCode ��� ���������
     * @return {@link Boolean}
     * @throws Exception
     */
    public Boolean aprrove(String activationCode) throws Exception {
        Entrant entrant = findByActivationCode(activationCode);
        if (entrant == null)
            return Boolean.FALSE;
        entrant.setActivationCode(null);
        update(entrant);
        return Boolean.TRUE;
    }

    /**
     * @param surname ������� ��� ������
     * @return {@link Entrant} ���� ���������� � ������ �������� ����������, null -
     *         � ��������� ������
     */
    public List<Entrant> findEntarnt(final String surname) {
        return entrantRepository.findBySurnameContainingIgnoreCase(surname);
    }

    /**
     * ����������� �����������, ������� �������� ���� ��������� �� ����� �����������
     * �����.
     * 
     * @param entrant {@link Entrant} ��� �����������.
     * @return {@link Boolean}
     * @throws MailException
     * @throws Exception
     */
    synchronized public Boolean register(Entrant entrant) throws MailException, Exception {
        SimpleDateFormat dFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");
        if (entrant.getEmail() != null && entrant.getType().getId() == Entrant.TYPE_MAIN)
            entrant.setActivationCode(UUID.randomUUID().toString());
        if (emailValidation && entrant.getEmail() != null) {
            String message = String.format(emailBody, entrant.getSurname(), entrant.getFirstname(),
                    entrant.getPatronymic(), entrant.getActivationCode(),
                    dFormat.format(entrant.getReservation().getReservationDate()));
            mailSender.send(entrant.getEmail(), emailHeader, message);
        }
        Reservation reservation = reservationService.findByTimestamp(entrant.getReservation().getReservationDate());
        if (null == reservation)
            return Boolean.FALSE;
        entrant.setReservation(reservation);
        create(entrant);
        return Boolean.TRUE;
    }
}
