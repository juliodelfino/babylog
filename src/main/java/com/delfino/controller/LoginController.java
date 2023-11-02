package com.delfino.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // handler method to handle login request
    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
