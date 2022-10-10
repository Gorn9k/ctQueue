package VSTU.ctQueue.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import VSTU.ctQueue.entity.Entrant;
import VSTU.ctQueue.entity.User;

@Service
public class UserValidator implements Validator {

    @Value("${operator.verification.code}")
    private String verificationCode;

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(final Class<?> clazz) {
        return Entrant.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        User user = (User) target;

        if (userService.findByUsername(user.getUsername()) != null)
            errors.rejectValue("username", "error.entrant.duplicate.username", new Object[] { user.getUsername() },
                    "User with this username already exist!");

        if (!user.getUsername().matches("[a-zA-Z0-9\\s]+"))
            errors.rejectValue("username", "error.entrant.wrongFormat.username", new Object[] { user.getUsername() },
                    "Username has contain only latin characters");

        if (user.getFirstname().length() > 50 || user.getSurname().length() > 50 || user.getPatronymic().length() > 50)
            errors.rejectValue("firstname", "error.creditals.oversize");

        if (null == user.getFirstname() || user.getFirstname().trim().isEmpty())
            errors.rejectValue("firstname", "error.user.empty.firstname");

        if (null == user.getSurname() || user.getSurname().trim().isEmpty())
            errors.rejectValue("surname", "error.user.empty.surname");

        if (null == user.getPatronymic() || user.getPatronymic().trim().isEmpty())
            errors.rejectValue("patronymic", "error.user.empty.patronymic");

        if (null == user.getVerificationCode() || user.getVerificationCode().trim().isEmpty())
            errors.rejectValue("verificationCode", "error.user.empty.verificationCode");

        if (null == user.getUsername() || user.getUsername().trim().isEmpty())
            errors.rejectValue("username", "error.user.empty.username");

        if (null == user.getPassword() || user.getPassword().trim().isEmpty())
            errors.rejectValue("password", "error.user.empty.Password");

        if (null == user.getPassword()
                || !user.getPassword().matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,}")) {
            errors.rejectValue("password", "error.user.wrongFormat.password");
        }

        if (null == user.getRPassword() || user.getRPassword().trim().isEmpty())
            errors.rejectValue("rPassword", "error.user.empty.rPassword");

        if (null != user.getPassword() && null != user.getRPassword()
                && !user.getPassword().equals(user.getRPassword()))
            errors.rejectValue("password", "error.user.password.doNotMatch");

        if (null != user.getVerificationCode() && !user.getVerificationCode().equals(verificationCode))
            errors.rejectValue("verificationCode", "error.user.verificationCode.doNotMatch");

    }

}
