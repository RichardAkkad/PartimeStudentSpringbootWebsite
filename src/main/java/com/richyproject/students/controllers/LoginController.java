package com.richyproject.students.controllers;


import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {



    @GetMapping("/LoginPage")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if ("no expiry".equals(error)){
            model.addAttribute("errorMessage","no expiry date for user was found");

        }
        else if ("expired".equals(error)) {
            model.addAttribute("errorMessage", "user details have expired");

        } else if ("hours".equals(error)) {
            model.addAttribute("errorMessage", "login only allowed during buisness hours");


        } else if ("username".equals(error)) {
            model.addAttribute("errorMessage", "invalid username");

        }

     else if ("password".equals(error)) {
        model.addAttribute("errorMessage", "invalid password");

    }


        return "LoginPage";


    }
}//why would we get null as error in what scenario?? because when we go to a restricted endpoint initially then error is not
//something like "expired, "hours etc.......Spring Security redirects to /LoginPage (not /LoginPage?error=something),
//No error parameter = error is null