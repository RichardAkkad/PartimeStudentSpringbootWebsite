package com.richyproject.students.controllers;

import com.richyproject.students.exceptions.StudentNameNotFoundException;
import com.richyproject.students.exceptions.ZeroException;
import com.richyproject.students.model.Student;
import com.richyproject.students.repository.StudentRepository;
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







//@Slf4j
@Controller
public class StudentController {

    @Autowired
    StudentService studentService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    private S3Service s3Service;
    
    @GetMapping("/index")
    public String homePage(){
        return "index";
    }






    @Autowired
    StudentService studentService;

    @Autowired
    StudentRepository studentRepository;
//-------------------------------
    @Autowired
    private S3Service s3Service;
//---------------------------------
    @GetMapping("/index")
    public String homePage(){
        return "index";
    }






    @GetMapping("/AddStudentPage")
    public String getaddStudentPage(Model model)
    {
        model.addAttribute("request", new Student());

        return studentService.addStudentServices();
    }
    @PostMapping("/SaveStudent")
    public String saveStudent(@ModelAttribute("request") Student student,
                              @RequestParam(value = "profilePictureFile", required = false) MultipartFile file,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {

        try {

//-----------------------------------------------------------------------------
            // Upload file to S3 if present
            if (file != null && !file.isEmpty()) {
                String filename = s3Service.uploadFile(file);
                student.setProfilePicture(filename);
            }
//-----------------------------------------------------------------------------
            return studentService.saveStudentServices(student);

        }

        catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
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
        try {
            return studentService.averageGradeResults(course, model);
        }
        catch (ZeroException e) {
            model.addAttribute("errorMessage", "Data error: " + e.getMessage());
            System.out.println("Exception occurred: " + e.getMessage());
            return "ErrorPage";
        }
    }








//---------------------------------------------------------
    @GetMapping("/SearchStudent")
    public String getsearchForStudent(){
        return studentService.searchForStudentServices();
    }

    @GetMapping("/SearchStudentById")
    public String studentIdResult(@RequestParam int id, Model model) throws StudentNameNotFoundException{
        try {
            return studentService.studentSearchIdResultServices(id, model);
        }
        catch (Exception e){
            model.addAttribute("ErrorMessage",e.getMessage());
            return "ErrorPage";
        }
    }
    //******** WHY HAVE I ONLY ONE PART TO THIS?
    @PostMapping("/SearchStudentByAgeRange")
    public String getstudentAgeRangeResult(@RequestParam String Course, @RequestParam int MinAge, @RequestParam int MaxAge,@RequestParam int MinGrade,@RequestParam int MaxGrade, Model model) {
        return studentService.searchStudentByAgeRangeServices(Course.toLowerCase(), MinAge, MaxAge,MinGrade,MaxGrade, model);
    }
//--------------------------------------------------------------







    @GetMapping("/DeletePage")
    public String getDeleteStudentPage() {

        return studentService.studentServicesDeletePage();
    }
    @DeleteMapping("/DeleteActualStudent")
    public String deleteActualStudent(@RequestParam int id)throws StudentNameNotFoundException {

        try {
            return studentService.deleteActualStudent(id);
        }
        catch (Exception e){
            return "ErrorPage";
        }

    }







    private void deleteProfilePicture(String filename) {
        try {
            String uploadDir = "C:/Users/richa/OneDrive/Desktop/student-images/";
            File fileToDelete = new File(uploadDir + filename);

            if (fileToDelete.exists()) {
                boolean deleted = fileToDelete.delete();
                System.out.println("Picture file deleted: " + deleted + " - " + filename);
            } else {
                System.out.println("Picture file not found: " + filename);
            }
        } catch (Exception e) {
            System.out.println("Error deleting picture: " + e.getMessage());
        }
    }








        @GetMapping("/UpdateStudent")
        public String getupdateStudentPage(Model model){
            return studentService.updateStudentPageServices(model);
            }

        @PatchMapping("/UpdateStudent")
        public String updateStudentResults(@RequestParam Integer id,@RequestParam String firstName, @RequestParam String surname,
                                           @RequestParam(required=false) Integer age, @RequestParam String course ,
                                           @RequestParam(required=false) Integer grade,@RequestParam String username,@RequestParam String local_Date, Model model) {
            //even though certain fields are not required something still needs to be returned for these parameters here from what i understand
            //if we use a primitive int instead of a Integer it might return  0 as it needs to return something but 0 is a value so wouldnt make sense, so using null is better for Integer i think

            System.out.println("patch is working up to here");
            System.out.println("ID is "+id);
            firstName = !firstName.trim().isEmpty()? firstName.toLowerCase() : null;
            surname = !surname.trim().isEmpty() ? surname.toLowerCase() : null;
            course = !course.trim().isEmpty() ? course.toLowerCase() : null;
            username = !username.trim().isEmpty() ? username.toLowerCase() : null;
            local_Date=!local_Date.trim().isEmpty()?local_Date.toLowerCase():null;

            try {

                return studentService.updateStudentServices(id, firstName, surname, age, course, grade, username, local_Date,model);

            } catch (Exception e) {
                String message = e.getMessage();
                model.addAttribute("errorMessage", message);
                return "errorPage";

            }


        }







































    }
