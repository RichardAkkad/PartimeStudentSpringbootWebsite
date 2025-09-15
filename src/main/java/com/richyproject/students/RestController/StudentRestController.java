package com.richyproject.students.RestController;

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




}
