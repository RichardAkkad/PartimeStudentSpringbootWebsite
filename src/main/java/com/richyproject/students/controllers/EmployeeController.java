package com.richyproject.students.controllers;

import com.richyproject.students.exceptions.EmployeeNameNotFoundException;
import com.richyproject.students.model.Employee;
import com.richyproject.students.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;


    @GetMapping("/AddNewEmployeePage")
    public String AddUser(Model model){

        return employeeService.addEmployeeServices(model);
    }

    @PostMapping("/SaveEmployee")
    public String saveUser(@ModelAttribute("request") Employee employee){

        return employeeService.saveEmployeeServices(employee);

    }


    @GetMapping("/DeleteEmployee")
    public String deleteUserForm() {


        return employeeService.deleteEmployeeServices();


    }


    @PostMapping("/DeleteActualEmployee")
    public String deleteActualEmployee(@RequestParam int id,Model model){
            try {
                return employeeService.deleteActualEmployeeServices(id);
            }
            catch(EmployeeNameNotFoundException e){

                model.addAttribute("errorMessage",e.getMessage());
                System.out.println("Exception occured: "+ e.getMessage());
                return "ErrorPage";


            }


    }





}
