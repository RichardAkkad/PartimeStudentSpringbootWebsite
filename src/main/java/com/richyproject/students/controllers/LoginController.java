package com.richyproject.students.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/LoginPage")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if(error!=null){
            model.addAttribute("errorMessage" ,"Invalid username or password");
        }
            return "LoginPage";

    }





}
