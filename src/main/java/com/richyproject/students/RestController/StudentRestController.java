package com.richyproject.students.RestController;

import com.richyproject.students.model.Student;
import com.richyproject.students.repository.StudentRepository;
import com.richyproject.students.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public class StudentRestController {
  @Autowired
    StudentRepository studentRepository;

 
    @PostMapping("/SaveAStudent")
    public ResponseEntity<Void> saveStudent(@RequestBody Student student)throws Exception {

        try{
            Student savedStudent=studentRepository.save(student);
            return ResponseEntity.status(201).build();


        }
        catch (Exception e) {
            return ResponseEntity.status(500).build();
        }

    }



    @GetMapping("/DeleteStudent")
    public ResponseEntity<?> deleteStudent(@RequestParam int id) {

        try {
            Optional<Student> deleteStudent = studentRepository.findById(id);
            studentRepository.deleteById(deleteStudent.get().getId());
            return ResponseEntity.status(201).header("message", "student saved").body(deleteStudent.get());
        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
            return ResponseEntity.status(404).body("no student with that id was found");

        }


    }


         



    @GetMapping("/searchStudentById")
    public ResponseEntity<?>  searchStudentViaId(@RequestParam String id) {
        try {
            Integer integerId=Integer.parseInt(id);
            Optional<Student> optionalStudent = studentRepository.findById(integerId);
            if (optionalStudent.isPresent()) {
               return ResponseEntity.ok(optionalStudent.get());

            }
            else{
                return ResponseEntity.status(404).body("the id "+id+ " was not found");
            }
        }
        catch(NumberFormatException e){
                return ResponseEntity.badRequest().body("invalid id format, enter digits as id only");//400
            }
        catch(Exception e){
            return ResponseEntity.status(500).body("internal server error occured");
        }





}
