package com.richyproject.students.controllers;

import com.richyproject.students.exceptions.StudentNameNotFoundException;
import com.richyproject.students.model.AccommodationProfile;
import com.richyproject.students.services.AccommodationProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AccommodationProfileController {

    @Autowired AccommodationProfileService accommodationProfileService;

    @GetMapping("/AccommodationProfile")
    public String getAccommodationProfilePage(Model model){
        model.addAttribute("AccommodationProfile",new AccommodationProfile());//lombok/Data must of created a no args constructor or maybe doesnt need to if its the only constructor used which i think is the case

        return accommodationProfileService.AccommodationProfilePageServices();

    }

    @PostMapping("/SaveAccommodationProfile")
    public String  saveAccommodationProfile(@ModelAttribute ("AccommodationProfile") AccommodationProfile accommProfile, @AuthenticationPrincipal UserDetails currentUser){

        return  accommodationProfileService.saveAccommodationProfilePageServices(accommProfile, currentUser);

    }

    @GetMapping("/FindRoommate")
    public String getRoommatePage(Model model){


        return accommodationProfileService.getRoommatePageServices();

    }

    @GetMapping("/SearchRoommate")
    public String searchRoommate(@RequestParam String smoker, @RequestParam int minAge, @RequestParam int maxAge, @RequestParam double minBudget,
                                 @RequestParam double  maxBudget, @RequestParam String pets, @RequestParam String dietaryPattern, @RequestParam String guests,
                                 @RequestParam List<String> genderPreference,@RequestParam List<String> course, Model model,@AuthenticationPrincipal  UserDetails currentUser ){
            // shoudldnt have called it course, should be courses as its a list of courses, cant change it now because the field in the accommodationprofile is set to course
        return accommodationProfileService.searchRoommatePageServices(smoker,minAge,maxAge,minBudget,maxBudget,pets,dietaryPattern,guests,genderPreference,course,model,currentUser);

    }

    @GetMapping("/UpdateAccommodationProfile")
    public String updateAccommodationProfilePage(){
        return accommodationProfileService.updateAccommodationProfilePageServices();

    }

    @PostMapping("/SaveUpdatedAccommodationProfile")
    public String saveUpdatedAccommodationProfilePage(@RequestParam String email,@RequestParam(required = false) Integer age,@RequestParam String gender,@RequestParam String course,
                                                 @RequestParam String smoker,@RequestParam String petOwner,@RequestParam String dietaryPattern, @RequestParam String overnightGuests,
                                                 @RequestParam(required = false)Double minBudget,@RequestParam(required = false)Double maxBudget,@AuthenticationPrincipal UserDetails currentUser){




        return accommodationProfileService.saveUpdatedAccommodationProfilePageServices(email,age,gender,course,smoker,petOwner,dietaryPattern,overnightGuests,minBudget,maxBudget,currentUser);

    }

    @GetMapping("/DeleteProfile")
    public String deleteAccommodationProfilePage(){

    return "DeleteAccommodationProfilePage";


    }
    @PostMapping("/DeleteAccommodationProfile")
    public String deleteAccommodationProfile(@RequestParam Integer studentId, Model model)throws StudentNameNotFoundException {
        try{
            return accommodationProfileService.deleteAccommodationProfileServices(studentId);
        }
        catch(Exception e){
            String message=e.getMessage();
            System.out.println(message);
            model.addAttribute("ErrorMessage",message);
            return "ErrorPage";

        }
    }












}
