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

        // Debug file information
        System.out.println("-----FILE DEBUG INFO-----");
        System.out.println("File is null: " + (file == null));
        if (file != null) {
            System.out.println("File is empty: " + file.isEmpty());
            System.out.println("File name: " + file.getOriginalFilename());
            System.out.println("File size: " + file.getSize());
            System.out.println("Content type: " + file.getContentType());
        }

        try {
            if (file != null && !file.isEmpty()) {
                System.out.println("Processing file upload...");
                String filename = saveProfilePicture(file);
                System.out.println("Saved filename: " + filename);
                student.setProfilePicture(filename);
            }
            else {
                System.out.println("No file to process");
            }

            System.out.println("Student profilePicture field: " + student.getProfilePicture());
            return studentService.saveStudentServices(student);

        }

        catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/AddStudentPage";
        }
    }







    private String saveProfilePicture(MultipartFile file) throws IOException {
        System.out.println("=== SAVE FILE DEBUG ===");

        // Save to Desktop/student-images folder
        String uploadDir = "C:/Users/richa/OneDrive/Desktop/student-images/";


        System.out.println("Upload directory: " + uploadDir);

        // Create folder if it doesn't exist
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            System.out.println("Directory created: " + created);
        }
        System.out.println("Directory exists: " + dir.exists());

        // Create unique filename
        String originalName = file.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        String uniqueName = System.currentTimeMillis() + extension;
        System.out.println("Unique filename: " + uniqueName);

        // Save the file
        File destFile = new File(uploadDir + uniqueName);
        System.out.println("Destination path: " + destFile.getAbsolutePath());

        file.transferTo(destFile);
        System.out.println("File saved successfully: " + destFile.exists());

        System.out.println("File size on disk: " + destFile.length()); // ADD THIS LINE and remove after
        System.out.println("EXACT FULL PATH: " + destFile.getCanonicalPath()); // ADD THIS and remove after


        return uniqueName; // Return just the filename
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

    @PostMapping("/SearchStudent")
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
        return studentService.searchStudentByAgeRangeServices(Course, MinAge, MaxAge,MinGrade,MaxGrade, model);
    }
//--------------------------------------------------------------







    @GetMapping("/DeletePage")
    public String getDeleteStudentPage() {

        return studentService.studentServicesDeletePage();
    }
    @PostMapping("/DeleteActualStudent")
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

        @PostMapping("/UpdateStudent")
        public String updateStudentResults(@RequestParam Integer id,@RequestParam  String firstName, @RequestParam  String surname,
                                           @RequestParam(required=false) Integer age, @RequestParam String course ,
                                           @RequestParam(required=false) Integer grade,@RequestParam String username, Model model) {
            //even though certain fields are not required something still needs to be returned for these parameters here from what i understand
            //if we use a primitive int instead of a Integer it might return  0 as it needs to return something but 0 is a value so wouldnt make sense, so using null is better for Integer i think


            firstName = (!firstName.trim().isEmpty()) ? firstName.toLowerCase() : null;
            surname = (!surname.trim().isEmpty()) ? surname.toLowerCase() : null;
            course = (!course.trim().isEmpty()) ? course.toLowerCase() : null;
            username = (!username.trim().isEmpty()) ? username.toLowerCase() : null;

            try {

                return studentService.updateStudentServices(id, firstName, surname, age, course, grade, username, model);

            } catch (Exception e) {
                String message = e.getMessage();
                model.addAttribute("errorMessage", message);
                return "errorPage";

            }


        }







































    }
