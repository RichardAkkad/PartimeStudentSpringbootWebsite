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
        //using the "profile" here which points to the same object as "acccommProfile" that had just binded to the fields in the method that called this method
        //already logged in at this point so can retrieve the username from the database which is what currentUser is used for and now we have the object from the database
        Optional<Student> student = studentRepository.findByUsername(currentUser.getUsername());
        profile.setStudent(student.get());//when put the student object in the argument of setStudent ("student" is a field from the accomm....) it then automatically know to save the "key" being the
        // id of the student object, ie JPA automatically extracts the student.getId() and stores it in the student_id column, you don't have to manually get the ID
        accommodationProfileRepository.save(profile);//then we save the accomodationProfile variable (that is assigned to the accomodationProfile object) as a whole
        //when getting "student_id" from the database it returns as a Student object and when saving Student object it saves as "student_id" as a id number in the database i think?
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


    public String saveUpdatedAccommodationProfilePageServices(String email, Integer age, String gender, String course, String smoker, String petOwner, String dietaryPattern, String overnightGuests,
                                                              Double minBudget, Double maxBudget, UserDetails currentUser) {

        Optional<AccommodationProfile> optionalAccommodationProfile = accommodationProfileRepository.findByStudentUsername(currentUser.getUsername());

        if (optionalAccommodationProfile.isPresent()) {
            optionalAccommodationProfile.get().setEmail(email.trim().equals("") ? optionalAccommodationProfile.get().getEmail() : email.toLowerCase());
            optionalAccommodationProfile.get().setMyGender(gender.trim().equals("") ? optionalAccommodationProfile.get().getMyGender() : gender.toLowerCase());
            optionalAccommodationProfile.get().setCourse(course.trim().equals("") ? optionalAccommodationProfile.get().getCourse() : course.toLowerCase());
            optionalAccommodationProfile.get().setSmoker(smoker.trim().equals("") ? optionalAccommodationProfile.get().getSmoker() : smoker.toLowerCase());
            optionalAccommodationProfile.get().setHasPets(petOwner.trim().equals("") ? optionalAccommodationProfile.get().getHasPets() : petOwner.toLowerCase());
            optionalAccommodationProfile.get().setDietaryPattern(dietaryPattern.trim().equals("") ? optionalAccommodationProfile.get().getDietaryPattern() : dietaryPattern.toLowerCase());
            optionalAccommodationProfile.get().setGuests(overnightGuests.trim().equals("") ? optionalAccommodationProfile.get().getGuests() : overnightGuests.toLowerCase());
            optionalAccommodationProfile.get().setAge(age == null ? optionalAccommodationProfile.get().getAge() : age);
            optionalAccommodationProfile.get().setMinBudget(minBudget == null ? optionalAccommodationProfile.get().getMinBudget() : minBudget);
            optionalAccommodationProfile.get().setMaxBudget(maxBudget == null ? optionalAccommodationProfile.get().getMinBudget() : maxBudget);
            accommodationProfileRepository.save(optionalAccommodationProfile.get());


        }
        return "PageSavedSuccessfully";
    }
           public String deleteAccommodationProfileServices(Integer accommodationProfileId)throws StudentNameNotFoundException{
                Optional<AccommodationProfile> optionalAccommodationProfile=accommodationProfileRepository.findById(accommodationProfileId);
               if(optionalAccommodationProfile.isPresent()){
                   accommodationProfileRepository.deleteById(accommodationProfileId);
                   return "StudentDeletedSuccessfully";

               }
               throw new StudentNameNotFoundException("studentId not found please try again");

           }














}
/*




 With my strong problem solving skills (as I have completed a mathematics and statistics degree ) and my newly learnt coding knowledge that i
 have taught myself in my spare time after work  as well as my dedication to overcoming obstacles and  learning new technologies, I believe I can contribute well as part of the team at Palantir



I'm interested in Palantir's Software Engineer role because it combines  technical challenges with meaningful impact.
What draws me most is working on complex data processing and software architecture problems while collaborating with engineers.
This role allows for technical specialization, which aligns with my passion for creating efficient solutions to challenging problems.
I'm excited to contribute to products solving important global challenges while growing my expertise in this demanding environment.
I feel that I will fit in well as part of the team because of my enthusiasm to learning new technologies, working part of a team and
contribute new ideas that be implemented in future projects


*/