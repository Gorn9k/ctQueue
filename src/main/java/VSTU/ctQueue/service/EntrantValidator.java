package VSTU.ctQueue.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import VSTU.ctQueue.entity.Entrant;
import VSTU.ctQueue.entity.Reservation;

@Service
public class EntrantValidator implements Validator {

    @Autowired
    private EntrantService entrantService;

    @Autowired
    private ReservationService reservationService;

    @Override
    public boolean supports(final Class<?> clazz) {
        return Entrant.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        Entrant entrant = (Entrant) target;

//        if (entrant.getActivationCode() != null || entrant.getId() != null || entrant.getRegDate() != null)
//            errors.rejectValue("activationCode", "error.entrant.badRequest");

        if (entrant.getEmail() != null && entrantService.findByEmail(entrant.getEmail()) != null)
            errors.rejectValue("email", "error.entrant.duplicate.email", new Object[] { entrant.getEmail() },
                    "Entrant with this email already exist!");

        if ((null == entrant.getEmail() || !entrant.getEmail()
                .matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))
                && entrant.getType().getId() == Entrant.TYPE_MAIN)
            errors.rejectValue("email", "error.entrant.wrongFormat.email", new Object[] { entrant.getEmail() },
                    "Wrong email format!");

        if (entrant.getFirstname().length() > 50 || entrant.getSurname().length() > 50
                || entrant.getPatronymic().length() > 50)
            errors.rejectValue("firstname", "error.creditals.oversize");

        if (null != entrantService.findByPhone(entrant.getPhone()))
            errors.rejectValue("phone", "error.entrant.duplicate.phone", new Object[] { entrant.getPhone() },
                    "Entrant with this phone number already exist!");

        if (String.valueOf(entrant.getPhone()).length() > 20 || String.valueOf(entrant.getPhone()).length() < 10)
            errors.rejectValue("phone", "error.entrant.wrongFormat.phone");

        if (null == entrant.getPhone())
            errors.rejectValue("phone", "error.entrant.empty.phone");

        if (null == entrant.getFirstname() || entrant.getFirstname().trim().isEmpty())
            errors.rejectValue("firstname", "error.entrant.empty.firstname");

        if (null == entrant.getSurname() || entrant.getSurname().trim().isEmpty())
            errors.rejectValue("surname", "error.entrant.empty.surname");

        if (null == entrant.getPatronymic() || entrant.getPatronymic().trim().isEmpty())
            errors.rejectValue("patronymic", "error.entrant.empty.patronymic");

        if ((null == entrant.getEmail() || entrant.getEmail().trim().isEmpty())
                && entrant.getType().getId() == Entrant.TYPE_MAIN)
            errors.rejectValue("email", "error.entrant.empty.email");

        Reservation reservation = entrant.getReservation();

        if (null == reservation || null == reservation.getId()) {
            errors.rejectValue("reservation.reservationDate", "error.entrant.empty.reservation.reservationDate");
//        }// else if (!reservationService.timeAroundCheck(reservation)
//                && (reservation = reservationService.findByDate(reservation.getReservationDate())) == null) {
//            errors.rejectValue("reservation.reservationDate",
//                    "error.entrant.reservation.reservationDate.time.aroundException", new Object[] { reservation },
//                    "New reservation is too close to another reservations");
        } else if (!reservationService.reservationIsValid(reservation.getId())) {
            errors.rejectValue("reservation", "error.entrant.reservation.date.isNotValid", new Object[] { reservation },
                    "Reservation is not valid");
        } else if (reservation != null
                && reservationService.findByTimestamp(reservation.getReservationDate()) == null) {
            errors.rejectValue("reservation", "error.entrant.reservation.date.DoesNotExist",
                    new Object[] { reservation }, "This date does not exist.");
        } else if (reservation != null && !reservationService.chekMainCounter(reservation) && entrant.isMain()) {
            errors.rejectValue("reservation", "error.entrant.reservation.mainCounter.count.isMax",
                    new Object[] { reservation }, "Main places is ended.");
        } else if (reservation != null && !reservationService.chekReserveCounter(reservation) && entrant.isReserve()) {
            errors.rejectValue("reservation", "error.entrant.reservation.reserveCounter.count.isMax",
                    new Object[] { reservation }, "Reserve places is ended.");
        }
    }
}
