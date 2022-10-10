package VSTU.ctQueue.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import VSTU.ctQueue.entity.Entrant;
import VSTU.ctQueue.service.EntrantService;
import VSTU.ctQueue.service.EntrantValidator;
import VSTU.ctQueue.service.ReservationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@ApiOperation(value = "Entrant register controller")
@RequestMapping("/entrant")
public class EntrantController {

    @Autowired
    private EntrantService entrantService;

    @Autowired
    private EntrantValidator entrantValidator;

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/getTimetable")
    public @ResponseBody Object getTimetable() throws ParseException {
        return reservationService.getTimetable(ReservationService.TIMETABLE_MAIN);
    }

    @GetMapping("/getReserveTimetable")
    public @ResponseBody Object getReserveTimetable() throws ParseException {
        return reservationService.getTimetable(ReservationService.TIMETABLE_RESERVE);
    }

    @PostMapping("/registration")
    @ApiOperation(value = "Main entrant register")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Registration successful") })
    public @ResponseBody Object register(@ModelAttribute Entrant entrant, final BindingResult result)
            throws MailException, Exception {
        try {
            entrantValidator.validate(entrant, result);
            if (result.hasErrors())
                return result.getAllErrors();
            entrantService.register(entrant);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage().toString());
        }
        return Boolean.TRUE;
    }

    @GetMapping("/{code}")
    @ApiOperation(value = "Controller for email confirmation")
    public String approve(@ApiParam("Entrant activation code") @PathVariable(required = true) final String code,
            Model model) throws Exception {
        return entrantService.aprrove(code) ? "activationAccept" : "activationFailure";
    }

    @GetMapping("/getCounters")
    public @ResponseBody Object getCounters() {
        return reservationService.getCounters();
    }
}
