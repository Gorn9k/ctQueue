package VSTU.ctQueue.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import VSTU.ctQueue.entity.User;
import VSTU.ctQueue.service.UserService;
import VSTU.ctQueue.service.UserValidator;

@Controller
@RequestMapping("/registration")
public class OperatorRegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    @PostMapping
    public @ResponseBody Object operatorRegistration(@ModelAttribute User user, final BindingResult result)
            throws Exception {
        try {
            userValidator.validate(user, result);
            if (result.hasErrors())
                return result.getAllErrors();
            userService.register(user);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage().toString());
        }
        return Boolean.TRUE;
    }

}
