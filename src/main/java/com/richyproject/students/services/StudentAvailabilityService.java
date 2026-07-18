package com.richyproject.students.services;


import com.richyproject.students.exceptions.StudentIdNotFoundException;
import com.richyproject.students.model.AccommodationProfile;
import com.richyproject.students.model.Student;
import com.richyproject.students.model.StudentWeeklyAvailability;
import com.richyproject.students.repository.AccommodationProfileRepository;
import com.richyproject.students.repository.StudentRepository;
import com.richyproject.students.repository.StudentWeeklyAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;

@Service
public class StudentAvailabilityService {

    public StudentAvailabilityService(studentWeeklyAvailabilityRepository,studentRepository,accommodationProfileRepository{
                                  
    this.studentWeeklyAvailabilityRepository=studentWeeklyAvailabilityRepository;
    this.studentRepository=studentRepository;
    this.accommodationProfileRepository=accommodationProfileRepository;
    }

    
    public String  saveStudentAvailabilityService(StudentWeeklyAvailability studentAvailability,UserDetails userDetails)
    {
        String studentUsername=userDetails.getUsername();
        Optional<Student> databaseStudent=studentRepository.findByUsername(studentUsername);
        studentAvailability.setStudent(databaseStudent.get());
        studentWeeklyAvailabilityRepository.save(studentAvailability);

        return "PageSavedSuccessfully";

    }
    public String studentAvailabilityServices(String course, String day, String startTime, String endTime, Model model) {

        double searchStart=Double.parseDouble(startTime);
        double searchEnd=Double.parseDouble(endTime);

        List<String> studentWeeklyAvailabilityResults =new ArrayList<>();//only certain students left in here now

        checkSlots(day,searchStart,searchEnd, studentWeeklyAvailabilityResults);

        model.addAttribute("students",studentWeeklyAvailabilityResults);
        return "StudentAvailabilitySearchResults";

    }

    public  void  checkSlots(String day,double searchStart, double searchEnd, List<String> studentWeeklyAvailabilityResults) {

        List<StudentWeeklyAvailability> available = switch (day) {
            case "monday" -> studentWeeklyAvailabilityRepository.findByMonday_AvailableTrue();
            case "tuesday" -> studentWeeklyAvailabilityRepository.findByTuesday_AvailableTrue();
            case "wednesday" -> studentWeeklyAvailabilityRepository.findByWednesday_AvailableTrue();
            case "thursday" -> studentWeeklyAvailabilityRepository.findByThursday_AvailableTrue();
            case "friday" -> studentWeeklyAvailabilityRepository.findByFriday_AvailableTrue();
            case "saturday" -> studentWeeklyAvailabilityRepository.findBySaturday_AvailableTrue();
            case "sunday" -> studentWeeklyAvailabilityRepository.findBySunday_AvailableTrue();
            default -> List.of();
        };

        if(!available.isEmpty()){

            for(StudentWeeklyAvailability studentAvailability : available){
                checkTimeConstraints(studentAvailability,day,searchStart,searchEnd,studentWeeklyAvailabilityResults);

            }

        }


    }
    private record TimeSlot(String start, String end) {}
    public  Map<String ,List<StudentAvailabilityService.TimeSlot>> weeklySlots(StudentWeeklyAvailability a) {
        return  Map.of("monday", List.of(new TimeSlot(a.getMonday_start1(), a.getMonday_end1()),
                                                     new TimeSlot(a.getMonday_start2(), a.getMonday_end2()),
                                                     new TimeSlot(a.getMonday_start3(), a.getMonday_end3())),
                                                  "tuesday",   List.of(new TimeSlot(a.getTuesday_start1(), a.getTuesday_end1()),
                                                    new TimeSlot(a.getTuesday_start2(), a.getTuesday_end2()),
                                                    new TimeSlot(a.getTuesday_start3(), a.getTuesday_end3())),
                "wednesday", List.of(new TimeSlot(a.getWednesday_start1(), a.getWednesday_end1()),
                new TimeSlot(a.getWednesday_start2(), a.getWednesday_end2()),
                new TimeSlot(a.getWednesday_start3(), a.getWednesday_end3())),
                "thursday",  List.of(new TimeSlot(a.getThursday_start1(), a.getThursday_end1()),
                new TimeSlot(a.getThursday_start2(), a.getThursday_end2()),
                new TimeSlot(a.getThursday_start3(), a.getThursday_end3())),
                "friday",    List.of(new TimeSlot(a.getFriday_start1(), a.getFriday_end1()),
                new TimeSlot(a.getFriday_start2(), a.getFriday_end2()),
                new TimeSlot(a.getFriday_start3(), a.getFriday_end3())),
                "saturday",  List.of(new TimeSlot(a.getSaturday_start1(), a.getSaturday_end1()),
                new TimeSlot(a.getSaturday_start2(), a.getSaturday_end2()),
                new TimeSlot(a.getSaturday_start3(), a.getSaturday_end3())),
                "sunday",    List.of(new TimeSlot(a.getSunday_start1(), a.getSunday_end1()),
                new TimeSlot(a.getSunday_start2(), a.getSunday_end2()),
                new TimeSlot(a.getSunday_start3(), a.getSunday_end3()))
    );



    }

    public void  checkTimeConstraints(StudentWeeklyAvailability studentAvailability,String day,double searchStart,double searchEnd,List<String> studentWeeklyAvailabilityListOfResults){
                List<TimeSlot> slots= weeklySlots(studentAvailability).get(day.toLowerCase());
                boolean check=false;
                for(TimeSlot slot:slots){
                    if(slot.start() != null && !slot.start().isEmpty()
                            && Double.parseDouble(slot.start().replace(":","."))<=searchStart &&
                            Double.parseDouble(slot.end().replace(":","."))>=searchEnd){
                       check=true;
                        break;
                    }
                }

                if(check) {

                    Student findStudent=studentAvailability.getStudent();//get the students and then get email of each student
                    AccommodationProfile accommodation=accommodationProfileRepository.findByStudent(findStudent);
                    studentWeeklyAvailabilityListOfResults.add(accommodation.getEmail());
                    }

    }

    public String deleteStudentWeeklyAvailabilityServices(Long id) throws StudentIdNotFoundException {

            if(!studentWeeklyAvailabilityRepository.existsById(id)){

            throw new StudentIdNotFoundException("id not found please try again");
        }

            studentWeeklyAvailabilityRepository.deleteById(id);

        return "pageSavedSuccessfully";

    }










}
