package VSTU.ctQueue.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/success")
    public @ResponseBody Principal authComplete(Principal principal) {
        return principal;
    }

    @GetMapping("/login")
    public @ResponseBody String authLogin() {
        return "You need to log in";
    }

    @GetMapping("/failure")
    public @ResponseBody Boolean authFailure() {
        return Boolean.FALSE;
    }

    @GetMapping("/logout")
    public @ResponseBody String authLogout() {
        return "You have loged out";
    }

}
