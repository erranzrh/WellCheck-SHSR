package com.SmartHealthRemoteSystem.SHSR.Login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/")
    public String index(){
        return "login";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

}

