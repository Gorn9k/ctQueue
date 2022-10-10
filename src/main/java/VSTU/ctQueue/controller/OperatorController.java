package VSTU.ctQueue.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import VSTU.ctQueue.entity.Entrant;
import VSTU.ctQueue.service.EntrantService;
import VSTU.ctQueue.service.EntrantUpdateValidator;
import VSTU.ctQueue.service.EntrantValidator;
import VSTU.ctQueue.service.ReportService;
import VSTU.ctQueue.service.ReservationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@RequestMapping("/operator")
public class OperatorController {

    @Autowired
    private EntrantService entrantService;

    @Autowired
    private EntrantValidator entrantValidator;

    @Autowired
    private EntrantUpdateValidator entrantUpdateValidator;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    public ReportService reportService;

    @Value("${operator.find.minLenght.surname}")
    private int minLenght;

    @GetMapping("/getReverseTimetable")
    public @ResponseBody Object test() {
        return reservationService.getReverseTimetable();
    }

    @GetMapping("/report/generate")
    public @ResponseBody ResponseEntity<Resource> generateReport(@RequestParam(required = true) String date)
            throws MalformedURLException, IOException, ParseException {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + date + ".xlsx" + "\"")
                .body(new UrlResource(reportService.generate(new SimpleDateFormat("yyyy-MM-dd").parse(date)).toURI()));
    }

    @PostMapping("/entrant/reserveRegistration")
    @ApiOperation(value = "Reserve entrant register controller")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Registration successful") })
    public @ResponseBody Object register(@ModelAttribute Entrant entrant, final BindingResult result)
            throws MailException, Exception {
        try {
            entrant.getType().setId(Entrant.TYPE_RESERVE);
            entrant.setEmail(null);
            entrantValidator.validate(entrant, result);
            if (result.hasErrors())
                return result.getAllErrors();
            entrantService.register(entrant);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage().toString());
        }
        return Boolean.TRUE;
    }

    @PostMapping("/entrant/update")
    @ApiOperation(value = "Entrant update controller")
    public @ResponseBody Object operatorUserUpdate(@ModelAttribute final Entrant entrant, final BindingResult result) {
        try {
            entrantUpdateValidator.validate(entrant, result);
            if (result.hasErrors())
                return result.getAllErrors();
            entrantService.update(entrant);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage().toString());
        }
        return Boolean.TRUE;
    }

    @GetMapping("/entrant/delete")
    public @ResponseBody Object operatorUserDelete(@RequestParam(required = true) final Long id) {
        try {
            if (!entrantService.entrantIsExist(id))
                return new ObjectError("Entrant", new String[] { "error.entrant.doNotExist" }, new Object[] { id },
                        "This entrant do not exist!");
            entrantService.delete(id);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage().toString());
        }
        return Boolean.TRUE;
    }

    @GetMapping("/entrant/find")
    public @ResponseBody Object findEntrant(@RequestParam(required = true, name = "surname") final String surname) {
        if (surname.length() < minLenght)
            return new ObjectError("Entrant", new String[] { "error.entrant.surname.tooShort" },
                    new Object[] { surname }, "Too short surname!");
        return entrantService.findEntarnt(surname);
    }
}
