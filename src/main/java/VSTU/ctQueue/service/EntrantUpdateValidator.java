package VSTU.ctQueue.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import VSTU.ctQueue.entity.Entrant;
import VSTU.ctQueue.entity.Reservation;

@Service
public class EntrantUpdateValidator implements Validator {

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

        if (entrant.getId() == null || !entrantService.entrantIsExist(entrant.getId()))
            errors.rejectValue("id", "error.entrant.doNotExist", new Object[] { entrant.getId() },
                    "This entrant do not exist!");

        if (entrantService.findByEmail(entrant.getEmail()) != null && !entrant.getEmail().isEmpty())
            errors.rejectValue("email", "error.entrant.duplicate.email", new Object[] { entrant.getEmail() },
                    "Entrant with this email already exist!");

        if (entrant.getEmail() != null && !entrant.getEmail().isEmpty()
                && !entrant.getEmail().matches(
                        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
                && entrant.getType().getId() == Entrant.TYPE_MAIN)
            errors.rejectValue("email", "error.entrant.wrongFormat.email", new Object[] { entrant.getEmail() },
                    "Wrong email format!");

//        if (entrant.getType() != null)
//            errors.rejectValue("type", "error.entrant.wrongFormat.type", new Object[] { entrant.getType().getId() },
//                    "Wrong entrant typeID!");

        if (entrant.getFirstname().length() > 50 && !entrant.getFirstname().isEmpty())
            errors.rejectValue("firstname", "error.firstname.oversize");

        if (entrant.getSurname().length() > 50 && !entrant.getSurname().isEmpty())
            errors.rejectValue("surname", "error.surname.oversize");

        if (entrant.getPatronymic().length() > 50 && !entrant.getSurname().isEmpty())
            errors.rejectValue("patronymic", "error.patronymic.oversize");

        if (null != entrantService.findByPhone(entrant.getPhone()) && entrant.getPhone() != null)
            errors.rejectValue("phone", "error.entrant.duplicate.phone", new Object[] { entrant.getPhone() },
                    "Entrant with this phone number already exist!");

        if ((String.valueOf(entrant.getPhone()).length() > 20 || String.valueOf(entrant.getPhone()).length() < 10)
                && entrant.getPhone() != null)
            errors.rejectValue("phone", "error.entrant.wrongFormat.phone");

        Reservation reservation = entrant.getReservation();

        if (reservation != null && reservationService.findByTimestamp(reservation.getReservationDate()) == null) {
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
