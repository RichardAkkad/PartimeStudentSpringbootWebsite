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
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class StudentAvailabilityService {

    @Autowired
    StudentWeeklyAvailabilityRepository studentWeeklyAvailabilityRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    AccommodationProfileRepository accommodationProfileRepository;

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

        List<StudentWeeklyAvailability> studentWeeklyAvailabilityList=studentWeeklyAvailabilityRepository.findAll();
        System.out.println(studentWeeklyAvailabilityList.size());

        List<String> studentWeeklyAvailabilityListOfResults =new ArrayList<>(Arrays.asList());


        for(StudentWeeklyAvailability studentsAvailability:studentWeeklyAvailabilityList){
                checkSlots(studentsAvailability,day,searchStart,searchEnd,studentWeeklyAvailabilityList, studentWeeklyAvailabilityListOfResults);

        }

        model.addAttribute("students",studentWeeklyAvailabilityListOfResults);
        return "StudentAvailabilitySearchResults";



    }

    public  void  checkSlots(StudentWeeklyAvailability studentsAvailability,String day,Double searchStart, Double searchEnd,List<StudentWeeklyAvailability> studentWeeklyAvailabilityList, List<String>studentWeeklyAvailabilityListOfResults) {
        if("monday".equals(day) && "on".equals(studentsAvailability.getMonday_available())||"tuesday".equals(day) && "on".equals(studentsAvailability.getTuesday_available())||
                "wednesday".equals(day) && "on".equals(studentsAvailability.getWednesday_available())||"thursday".equals(day) && "on".equals(studentsAvailability.getThursday_available())||
                "friday".equals(day) && "on".equals(studentsAvailability.getFriday_available())||"saturday".equals(day) && "on".equals(studentsAvailability.getSaturday_available())||
                "sunday".equals(day) && "on".equals(studentsAvailability.getSunday_available())){

                   checkTimeConstraints(studentsAvailability,day,searchStart,searchEnd,studentWeeklyAvailabilityList,studentWeeklyAvailabilityListOfResults);
        }

    }

    public void  checkTimeConstraints(StudentWeeklyAvailability studentAvailability, String day,Double searchStart,Double searchEnd,List<StudentWeeklyAvailability> studentWeeklyAvailabilityList,List<String> studentWeeklyAvailabilityListOfResults){
       boolean check=false;

        if(day.equals("monday")){
                         if ((Double.parseDouble(studentAvailability.getMonday_start1().replace(":","."))<=searchStart && (Double.parseDouble(studentAvailability.getMonday_end1().replace(":","."))>=searchEnd) && studentAvailability.getMonday_start1()!="")
                                || (Double.parseDouble(studentAvailability.getMonday_start2().replace(":","."))<=searchStart && (Double.parseDouble(studentAvailability.getMonday_end2().replace(":","."))>=searchEnd)&&studentAvailability.getMonday_start2()!="")
                                || (Double.parseDouble(studentAvailability.getMonday_start3().replace(":","."))<= searchStart&& (Double.parseDouble(studentAvailability.getMonday_end3().replace(":","."))>=
                                searchEnd)&&studentAvailability.getMonday_start1()!="")){
                                check=true;
                            }
        }
        if(day.equals("tuesday")){
            if ((Double.parseDouble(studentAvailability.getTuesday_start1().replace(":","."))<=searchStart && (Double.parseDouble(studentAvailability.getTuesday_end1().replace(":","."))>=searchEnd) && studentAvailability.getTuesday_start1()!="")
                    || (Double.parseDouble(studentAvailability.getTuesday_start2().replace(":","."))<=searchStart && (Double.parseDouble(studentAvailability.getTuesday_end2().replace(":","."))>=searchEnd)&&studentAvailability.getTuesday_start2()!="")
                    || (Double.parseDouble(studentAvailability.getTuesday_start3().replace(":","."))<= searchStart&& (Double.parseDouble(studentAvailability.getTuesday_end3().replace(":","."))>=
                    searchEnd)&&studentAvailability.getTuesday_start3()!="")){
                check=true;
            }
        }
        if(day.equals("wednesday")){
            if ((Double.parseDouble(studentAvailability.getWednesday_start1().replace(":","."))<=searchStart && (Double.parseDouble(studentAvailability.getWednesday_end1().replace(":","."))>=searchEnd) && studentAvailability.getWednesday_start1()!="")
                    || (Double.parseDouble(studentAvailability.getWednesday_start2().replace(":","."))<=searchStart && (Double.parseDouble(studentAvailability.getWednesday_end2().replace(":","."))>=searchEnd)&&studentAvailability.getWednesday_start2()!="")
                    || (Double.parseDouble(studentAvailability.getWednesday_start3().replace(":","."))<= searchStart&& (Double.parseDouble(studentAvailability.getWednesday_end3().replace(":","."))>=
                    searchEnd)&&studentAvailability.getWednesday_start3()!="")){
                check=true;
            }
        }
        if(day.equals("thursday")){
            if ((Double.parseDouble(studentAvailability.getThursday_start1().replace(":","."))<=searchStart && (Double.parseDouble(studentAvailability.getThursday_end1().replace(":","."))>=searchEnd) && studentAvailability.getThursday_start1()!="")
                    || (Double.parseDouble(studentAvailability.getThursday_start2().replace(":","."))<=searchStart && (Double.parseDouble(studentAvailability.getThursday_end2().replace(":","."))>=searchEnd)&&studentAvailability.getThursday_start2()!="")
                    || (Double.parseDouble(studentAvailability.getThursday_start3().replace(":","."))<= searchStart&& (Double.parseDouble(studentAvailability.getThursday_end3().replace(":","."))>=
                    searchEnd)&&studentAvailability.getThursday_start3()!="")){
                check=true;
            }
        }
        if(day.equals("friday")){
            if ((Double.parseDouble(studentAvailability.getFriday_start1().replace(":","."))<=searchStart && (Double.parseDouble(studentAvailability.getFriday_end1().replace(":","."))>=searchEnd) && studentAvailability.getFriday_start1()!="")
                    || (Double.parseDouble(studentAvailability.getFriday_start2().replace(":","."))<=searchStart && (Double.parseDouble(studentAvailability.getFriday_end2().replace(":","."))>=searchEnd)&&studentAvailability.getFriday_start2()!="")
                    || (Double.parseDouble(studentAvailability.getFriday_start3().replace(":","."))<= searchStart&& (Double.parseDouble(studentAvailability.getFriday_end3().replace(":","."))>=
                    searchEnd)&&studentAvailability.getFriday_start3()!="")){
                check=true;
            }
        }
        if(day.equals("saturday")){
            if ((Double.parseDouble(studentAvailability.getSaturday_start1().replace(":","."))<=searchStart && (Double.parseDouble(studentAvailability.getSaturday_end1().replace(":","."))>=searchEnd) && studentAvailability.getSaturday_start1()!="")
                    || (Double.parseDouble(studentAvailability.getSaturday_start2().replace(":","."))<=searchStart && (Double.parseDouble(studentAvailability.getSaturday_end2().replace(":","."))>=searchEnd)&&studentAvailability.getSaturday_start2()!="")
                    || (Double.parseDouble(studentAvailability.getSaturday_start3().replace(":","."))<= searchStart&& (Double.parseDouble(studentAvailability.getSaturday_end3().replace(":","."))>=
                    searchEnd)&&studentAvailability.getSaturday_start3()!="")){
                check=true;
            }
        }
        if(day.equals("sunday")){
            if ((Double.parseDouble(studentAvailability.getSunday_start1().replace(":","."))<=searchStart && (Double.parseDouble(studentAvailability.getSunday_end1().replace(":","."))>=searchEnd) && studentAvailability.getSunday_start1()!="")
                    || (Double.parseDouble(studentAvailability.getSunday_start2().replace(":","."))<=searchStart && (Double.parseDouble(studentAvailability.getSunday_end2().replace(":","."))>=searchEnd)&&studentAvailability.getSunday_start2()!="")
                    || (Double.parseDouble(studentAvailability.getSunday_start3().replace(":","."))<= searchStart&& (Double.parseDouble(studentAvailability.getSunday_end3().replace(":","."))>=
                    searchEnd)&&studentAvailability.getSunday_start3()!="")){
                check=true;
            }
        }





    if(check) {

        Student findStudent=studentAvailability.getStudent();
        AccommodationProfile accommodation=accommodationProfileRepository.findByStudent(findStudent);
        System.out.println(accommodation);
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
