package com.richyproject.students.controllers;

import com.richyproject.students.exceptions.StudentIdNotFoundException;
import com.richyproject.students.exceptions.StudentNameNotFoundException;
import com.richyproject.students.exceptions.CourseNotFoundException;
import com.richyproject.students.model.Student;
import com.richyproject.students.repository.StudentRepository;
import com.richyproject.students.services.S3Service;
import com.richyproject.students.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.File;
import java.io.IOException;


import org.springframework.validation.BindingResult;

@Controller
public class StudentController {

     private final StudentService studentService;
    private final S3Service s3Service;

    public StudentController(StudentService studentService, S3Service s3Service) }

        this.studentService = studentService;
        this.s3Service = s3Service;
    }

    public String homePage(){
        return "index";
    }






    @GetMapping("/AddStudentPage")
    public String getaddStudentPage(Model model) {

        model.addAttribute("request", new Student());
        return studentService.addStudentServices();


    }


    @PostMapping("/SaveStudent")
    public String saveStudent(@ModelAttribute("request") Student student,
                              @RequestParam(value = "profilePictureFile", required = false) MultipartFile file,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {

        try{

//-----------------------------------------------------------------------------
            // Upload file to S3 if present
            if (file != null && !file.isEmpty()) {
                String filename = s3Service.uploadFile(file);
                student.setProfilePicture(filename);
            }
//-----------------------------------------------------------------------------
            return studentService.saveStudentServices(student);
        }

        catch (IOException | RuntimeException e) {
            return "redirect:/AddStudentPage";
        }
    }

    @GetMapping("/AgeRange")
    public String getAgeRangePage(){
        return "AgeRange";
    }

    @GetMapping("AgeRangeResults")
    public String searchForSpecificAge(@RequestParam int age, Model model){
        return studentService.findAgefromServices(age, model);
    }

    @GetMapping("/AgePercentage")
    public String AgePercentagePage(){
        return "AgeRangePercentagePage";
    }

    @GetMapping("GetAgeRangePercentageResults")
    public String getAgeRangePercentageResults(@RequestParam int age, Model model){
            return studentService.getAgeRangePercentageResultsServices(age, model);

    }

    @GetMapping("AverageGrades")
    public String getAverageGrades(){
        return "AverageGrades";
    }

    @GetMapping("AverageGradeResults")
    public String averageGradeResults(@RequestParam String course, Model model) {
            return studentService.averageGradeResults(course, model);

    }




    @GetMapping("/ErrorPage")
    public String errorPage(@RequestParam(required = false)String error, Model model){
        if("access".equals(error)) {
            model.addAttribute("errorMessage", "you dont have permission to access this page");
        }
        return "ErrorPage";
    }



//---------------------------------------------------------
    @GetMapping("/SearchStudent")
    public String getsearchForStudent(){
        return studentService.searchForStudentServices();
    }

    @GetMapping("/SearchStudentById")
    public String studentIdResult(@RequestParam int id, Model model) throws StudentIdNotFoundException{
            return studentService.studentSearchIdResultServices(id, model);
    }
    //******** WHY HAVE I ONLY ONE PART TO THIS?
    @GetMapping("/SearchStudentByAgeRange")
    public String getstudentAgeRangeResult(@RequestParam String Course, @RequestParam int minAge, @RequestParam int maxAge,@RequestParam int minGrade,@RequestParam int maxGrade, Model model) {
        return studentService.searchStudentByAgeRangeServices(Course.toLowerCase(), minAge, maxAge,minGrade,maxGrade, model);
    }
//--------------------------------------------------------------

    @GetMapping("/DeleteStudentPage")
    public String getDeleteStudentPage() {

        return studentService.studentServicesDeletePage();
    }
    @PostMapping("/DeleteActualStudent")
    public String deleteActualStudent(@RequestParam int id)throws StudentIdNotFoundException {
            return studentService.deleteActualStudent(id);
    }



        @GetMapping("/UpdateStudent")
        public String getupdateStudentPage(Model model){
            return studentService.updateStudentPageServices(model);
            }

    public String cleanOrNull(String value){
        return value==null || value.trim().equals("")?null:value.toLowerCase();

    }

        @PostMapping("/UpdateStudent")
        public String updateStudentResults(@RequestParam Integer id,@RequestParam String firstName, @RequestParam String surname,
                                           @RequestParam(required=false) Integer age, @RequestParam String course ,
                                           @RequestParam(required=false) Integer grade,@RequestParam String username,@RequestParam String local_Date, Model model) throws StudentIdNotFoundException {
            //even though certain fields are not required something still needs to be returned for these parameters here from what i understand
            //if we use a primitive int instead of a Integer it might return  0 as it needs to return something but 0 is a value so wouldnt make sense, so using null is better for Integer i think

            firstName = cleanOrNull(firstName);
            surname = cleanOrNull(surname);
            course = cleanOrNull(course);
            username = cleanOrNull(username);
            local_Date=cleanOrNull(local_Date);

                return studentService.updateStudentServices(id, firstName, surname, age, course, grade, username, local_Date,model);



        }







































    }

