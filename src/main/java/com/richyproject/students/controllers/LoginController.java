package com.richyproject.students.controllers;


import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    /*@GetMapping("/LoginPage")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {//required=false means it doesnt need to have "error" as a query parameter name,
        if(error!=null){//The part error=true is a query parameter — this is what Spring's @RequestParam("error") picks up. whatever is assigned to "name" in a html page
            model.addAttribute("errorMessage" ,"Invalid username or password");//becomes the query parameter name when a form is submitted (especially via GET
        }//**whatever is assigned to "name" in the html page is the query parameter name
            return "LoginPage";

    }

     */

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
