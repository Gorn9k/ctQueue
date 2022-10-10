package VSTU.ctQueue.controller;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import VSTU.ctQueue.model.ReservationGenerateModel;
import VSTU.ctQueue.service.ReservationGenerateModelValidator;
import VSTU.ctQueue.service.ReservationService;

@Controller
@RequestMapping("/reservation")
@Validated
public class ReservationController {

    @Autowired
    private ReservationGenerateModelValidator generateModelValidator;

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/delete")

    public @ResponseBody Object deleteDate(@RequestParam(required = true) String date) {
        try {
            SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (!reservationService.checkRemovable(dayFormat.parse(date)))
                return new ObjectError("reservation.reservationDate",
                        new String[] { "error.reservation.date.hasEntrants" }, new Object[] { date },
                        "This date has entrants");
            reservationService.deleteByDate(dayFormat.parse(date));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage().toString());
        }
        return Boolean.TRUE;
    }

    @PostMapping("/generate")
    public @ResponseBody Object generateTimeTable(@ModelAttribute final ReservationGenerateModel generateModel,
            final BindingResult result) {
        try {
            generateModelValidator.validate(generateModel, result);
            if (result.hasErrors())
                return result.getAllErrors();
            reservationService.generateTimetable(generateModel);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage().toString());
        }
        return Boolean.TRUE;
    }

}
