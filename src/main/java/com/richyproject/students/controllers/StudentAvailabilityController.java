package com.richyproject.students.controllers;

import com.richyproject.students.model.StudentWeeklyAvailability;
import com.richyproject.students.services.StudentAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
    public class StudentAvailabilityController {

        @Autowired
        StudentAvailabilityService studentAvailabilityService;

        @GetMapping("/StudentAvailabilityPage")
        public String getStudentAvailabilityPage(Model model){

            model.addAttribute("studentavailability",new StudentWeeklyAvailability());
            return "StudentScheduleAvailabilityPage";

        }

        @PostMapping("/SaveStudentAvailability")
        public String saveStudentAvailability(@ModelAttribute ("studentavailability") StudentWeeklyAvailability studentAvailability, @AuthenticationPrincipal UserDetails userDetails){


          return studentAvailabilityService.saveStudentAvailabilityService(studentAvailability,userDetails);


        }
        @GetMapping("/SearchStudents")
        public String searchStudentPage() {
            return "StudentAvailabilitySearch";

        }

        @GetMapping("/StudentAvailabilitySearch")
        public String StudentAvailabilitySearch(@RequestParam String course,@RequestParam String day,@RequestParam String startTime,@RequestParam String endTime,Model model){
            startTime=startTime.replace(":",".");
            endTime=endTime.replace(":",".");

            return studentAvailabilityService.studentAvailabilityServices(course,day,startTime,endTime,model);

        }
        @GetMapping("/DeleteStudentsAvailability")
        public String deleteStudentAvailabilityPage(){
            return "DeleteStudentWeeklyAvailabilityPage";
        }

        @PostMapping("/DeleteStudentAvailability")
        public String deleteStudentsAvailability(@RequestParam(required = false) Long availabilityId,Model model){
            if(availabilityId ==null) {
                return "DeleteStudentWeeklyAvailabilityPage";
            }
            try {return studentAvailabilityService.deleteStudentWeeklyAvailabilityServices(availabilityId);}
            catch (Exception e){
                System.out.println(e.getMessage());
                model.addAttribute("errorMessage",e.getMessage());
                return "errorPage";
            }

        }







    }
