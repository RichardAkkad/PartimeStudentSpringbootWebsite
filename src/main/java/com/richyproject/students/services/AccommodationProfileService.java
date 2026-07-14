package com.richyproject.students.services;

import com.richyproject.students.exceptions.StudentNameNotFoundException;
import com.richyproject.students.model.AccommodationProfile;
import com.richyproject.students.model.Student;
import com.richyproject.students.repository.AccommodationProfileRepository;
import com.richyproject.students.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;


@Service
public class AccommodationProfileService {

    @Autowired
    AccommodationProfileRepository accommodationProfileRepository;

    @Autowired
    StudentRepository studentRepository;


    public String AccommodationProfilePageServices() {

        return "AccommodationProfilePage";

    }

    public String saveAccommodationProfilePageServices(AccommodationProfile profile, UserDetails currentUser) {
        profile.setSmoker(profile.getSmoker().toLowerCase());
        profile.setGuests(profile.getGuests().toLowerCase());
        profile.setHasPets(profile.getHasPets().toLowerCase());
        profile.setCourse(profile.getCourse().toLowerCase());
        profile.setEmail(profile.getEmail().toLowerCase());
        profile.setMyGender(profile.getMyGender().toLowerCase());
        profile.setStudent(
                studentRepository.findByUsername(currentUser.getUsername())
                        .orElseThrow(() -> new UsernameNotFoundException("username not found"))
        );
        accommodationProfileRepository.save(profile);
        return "PageSavedSuccessfully";


    }


    public String getRoommatePageServices() {
        return "FindRoommatesPage";
    }

    public String searchRoommatePageServices(String smoker, int minAge, int maxAge, double minBudget, double maxBudget, String pets,
                                             String dietaryPattern, String guests, List<String> genderPreference, List<String> courses, Model model, UserDetails userDetails) {
        //we dont have any empty strings "" or null assigned to any of the parameter variables because no fields on the form are left empty
        List<AccommodationProfile> accomodationProfileList = accommodationProfileRepository.findAll();
        //********
        System.out.println(accomodationProfileList.size());


        String smokerLower = smoker.toLowerCase();
        String petsLower = pets.toLowerCase();
        String guestsLower = guests.toLowerCase();
        String dietaryPatternLower = dietaryPattern.toLowerCase();

        List<String> coursesLower = courses.stream().map(String::toLowerCase).toList();
        List<String> genderPreferenceLower = genderPreference.stream().map(String::toLowerCase).toList();


        Predicate<AccommodationProfile> studentGender = obj -> {
            return genderPreferenceLower.contains(obj.getMyGender().toLowerCase());

        };

        Predicate<AccommodationProfile> studentCourse = obj -> {
            return coursesLower.contains(obj.getCourse().toLowerCase());

        };

        List<String> filteredList = accomodationProfileList.stream().filter(obj -> (obj.getAge() >= minAge
                        && obj.getAge() <= maxAge) && (obj.getMinBudget() <= minBudget && obj.getMaxBudget() >= maxBudget) && (obj.getSmoker().toLowerCase().equals(smokerLower)) &&
                (obj.getGuests().toLowerCase().equals(guestsLower)) && (obj.getHasPets().toLowerCase().equals(petsLower)) && (obj.getDietaryPattern().toLowerCase().equals(dietaryPatternLower)))
                .filter(studentGender).filter(studentCourse).map(obj -> obj.getEmail()).toList();


        model.addAttribute("list", filteredList);


        return "RoommatePreferenceResults";

    }

    public String updateAccommodationProfilePageServices() {
        return "UpdateAccommodationProfilePage";
    }
 public <T> T orDefault(T value, T databaseValue) {

        return value == null || value.toString().trim().isEmpty() ? databaseValue : value;
    }

    public String saveUpdatedAccommodationProfilePageServices(String email, Integer age, String gender, String course, String smoker, String petOwner, String dietaryPattern, String overnightGuests,
                                                              Double minBudget, Double maxBudget, UserDetails currentUser) throws StudentIdNotFoundException{

         AccommodationProfile accommodationProfile= accommodationProfileRepository.findByStudentUsername(currentUser.getUsername()).orElseThrow(()->new StudentIdNotFoundException("student id not found"));


        accommodationProfile.setEmail(orDefault(email,accommodationProfile.getEmail()));
        accommodationProfile.setMyGender(orDefault(gender,accommodationProfile.getMyGender()));
        accommodationProfile.setCourse(orDefault(course,accommodationProfile.getCourse()));
        accommodationProfile.setSmoker(orDefault(smoker,accommodationProfile.getSmoker()));
        accommodationProfile.setHasPets(orDefault(petOwner,accommodationProfile.getMyGender()));
        accommodationProfile.setDietaryPattern(orDefault(dietaryPattern,accommodationProfile.getMyGender()));
        accommodationProfile.setGuests(orDefault(gender,accommodationProfile.getGuests()));


            accommodationProfile.setAge(age == null || age.toString().trim().isEmpty()? accommodationProfile.getAge() : age);
            accommodationProfile.setMinBudget(minBudget == null || minBudget.toString().trim().isEmpty() ? accommodationProfile.getMinBudget() : minBudget);
            accommodationProfile.setMaxBudget(maxBudget == null || maxBudget.toString().trim().isEmpty()? accommodationProfile.getMaxBudget(): maxBudget);
            accommodationProfileRepository.save(accommodationProfile);



        return "PageSavedSuccessfully";
    }
           public String deleteAccommodationProfileServices(Integer accommodationProfileId)throws StudentNameNotFoundException{
               if(!accommodationProfileRepository.existsById(accommodationProfileId)) {
                   throw new StudentNameNotFoundException("studentId not found please try again");

               }
                   accommodationProfileRepository.deleteById(accommodationProfileId);
                   return "StudentDeletedSuccessfully";

           }














}








