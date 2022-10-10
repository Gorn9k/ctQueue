package VSTU.ctQueue.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import VSTU.ctQueue.entity.Reservation;
import VSTU.ctQueue.model.ReservationGenerateModel;


@Service
public class ReservationGenerateModelValidator implements Validator {

    @Autowired
    private ReservationService reservationService;

    @Override
    public boolean supports(final Class<?> clazz) {
        return ReservationGenerateModel.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        ReservationGenerateModel generateModel = (ReservationGenerateModel) target;

        if ((null == generateModel.getStartDate()
                || !generateModel.getStartDate().matches("\\d{4}(-|\\/)\\d{2}(-|\\/)\\d{2}")))
            errors.rejectValue("startDate", "error.reservationGenerateModel.wrongFormat.startDate",
                    new Object[] { generateModel.getStartDate() }, "Wrong date format!");

        if ((null == generateModel.getEndDate()
                || !generateModel.getEndDate().matches("\\d{4}(-|\\/)\\d{2}(-|\\/)\\d{2}")))
            errors.rejectValue("endDate", "error.reservationGenerateModel.wrongFormat.endDate",
                    new Object[] { generateModel.getStartDate() }, "Wrong date format!");

        if ((null == generateModel.getStartTime()
                || !generateModel.getStartTime().matches("^(([0,1][0-9])|(2[0-3])):[0-5][0-9]$")))
            errors.rejectValue("startTime", "error.reservationGenerateModel.wrongFormat.startTime",
                    new Object[] { generateModel.getStartTime() }, "Wrong time format!");

        if ((null == generateModel.getEndTime()
                || !generateModel.getEndTime().matches("^(([0,1][0-9])|(2[0-3])):[0-5][0-9]$")))
            errors.rejectValue("endTime", "error.reservationGenerateModel.wrongFormat.endTime",
                    new Object[] { generateModel.getEndTime() }, "Wrong time format!");
        if (null == generateModel.getInterval())
            errors.rejectValue("interval", "error.reservationGenerateModel.empty.interval",
                    new Object[] { generateModel.getInterval() }, "Interval can't be emty!");

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if (df.parse(generateModel.getStartDate()).getTime() > df.parse(generateModel.getEndDate()).getTime())
                errors.rejectValue("startDate", "error.reservationGenerateModel.startDate.wrongValue",
                        new Object[] { generateModel.getStartDate(), generateModel.getEndDate() },
                        "Wrong data values!");
            df = new SimpleDateFormat("HH:MM");
            if (df.parse(generateModel.getStartTime()).getTime() > df.parse(generateModel.getEndTime()).getTime())
                errors.rejectValue("startTime", "error.reservationGenerateModel.startTime.wrongValue",
                        new Object[] { generateModel.getStartTime(), generateModel.getEndTime() },
                        "Wrong time values!");
            if (!reservationService.checkTimetable(generateModel))
                errors.rejectValue("startDate", "error.reservationGenerateModel.isExist",
                        new Object[] { generateModel }, "Reservation from this interval has already exist!");
        } catch (ParseException e) {
        } catch (Exception e) {
        }

    }

}
