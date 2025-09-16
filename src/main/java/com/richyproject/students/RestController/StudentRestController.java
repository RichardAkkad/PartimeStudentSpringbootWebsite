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
    public ResponseEntity<Student> saveStudent(@RequestBody Student student)throws Exception {

        try{
            Student savedStudent=studentRepository.save(student);
            return ResponseEntity.ok(savedStudent);


        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }

    }

    @GetMapping("/DeleteStudent")
    public ResponseEntity<?> deleteStudent(@RequestParam int id){

        try{
            Optional<Student> deleteStudent=studentRepository.findById(id);
            studentRepository.deleteById(deleteStudent.get().getId());
            return ResponseEntity.status(200).header("message","student saved").body(deleteStudent.get());
        }
        catch(Exception e){
            return ResponseEntity.status(404).body("no student with that id was found");

        }



     }





}
