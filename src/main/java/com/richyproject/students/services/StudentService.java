package com.richyproject.students.services;


import com.richyproject.students.Enum.Role;
import com.richyproject.students.exceptions.StudentIdNotFoundException;
import com.richyproject.students.exceptions.StudentNameNotFoundException;
import com.richyproject.students.exceptions.CourseNotFoundException;
import com.richyproject.students.model.Student;
import com.richyproject.students.repository.AccommodationProfileRepository;
import com.richyproject.students.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

@Service
public class StudentService {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccommodationProfileRepository accommodationProfileRepository;

//---------------------------
    @Autowired
    S3Service s3Service;
//---------------------------


    public String addStudentServices(){
        return "AddStudent";

    }

    public String saveStudentServices(Student student){
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        student.setRole(Role.STUDENT);
            studentRepository.save(student);
            return "PageSavedSuccessfully";


    }

    public String studentServicesDeletePage(){
        return "DeleteStudentPage";
    }
    public String deleteActualStudent(Integer id) throws StudentIdNotFoundException {


            Student student=studentRepository.findById(id).orElseThrow(()->new StudentIdNotFoundException("Id not found please try again"));

//-----------------------------------------------------------------------------

            // Get the picture filename BEFORE deleting the student
            String profilePicture = student.getProfilePicture();

            // Delete student from database
            studentRepository.deleteById(id);

            // Delete the picture file from S3 if it exists
            if (profilePicture != null && !profilePicture.isEmpty()) {
                s3Service.deleteFile(profilePicture);
            }
//------------------------------------------------------------------------------
            return "StudentDeletedSuccessfully";
        //}
    }

    public String findAgefromServices(int age, Model model) {
        List<Student> studentList = studentRepository.findAll();

        Function<Student, String> lambVar = student -> String.valueOf(student.getId());

        List<String> info = studentList.stream().filter(student  -> student.getAge() < age).map(Student::getId).map(String::valueOf).toList();


        String message="the following id's are for the student's whos age is " + age + " and below is..... "+ String.join(", ",info);

        model.addAttribute("ageMessage", message);


        return "AgeRangeResults";


    }

    public String getAgeRangePercentageResultsServices(int age, Model model){

        List<Student> studentList=studentRepository.findAll();


        List<Integer> ageList =new ArrayList<>();
        studentList.stream().filter(student->student.getAge()<=age).forEach(student->ageList.add(student.getAge()));

        String message= ageList.isEmpty()?"unfortunately no students of that age "+age+" or less were found":(ageList.size()*100)/ studentList.size()+"% of the students are under the age of "+age;
        //dont think there is a point having a ternary sentence here if (inside a try block as not possible to have a arithmetic exception), because if its empty which
        //means ageLi.isEmpty size is zero so that takes care of the 0, therefore "ageLi.size()*100)/li.size()" cannot be "0/li.size()"


        model.addAttribute("agePercentageMessage", message);//basicly "message" is assigned to the variable "agePercentageMessage", also wont need to convert it into a toString as its already a
        return "AgeRangePercentageResults";//String which is what thymleaf would do if its not a String, it would use toString on it.


    }

    public String averageGradeResults(String course,Model model)throws CourseNotFoundException {

        List<Student> studentList=studentRepository.findAll();

        OptionalDouble average=studentList.stream().filter(obj->obj.getCourse().equals(course)).mapToInt(Student::getGrade).average();

        //"average" returns a OptionalDouble, but not a raw Double , if you want the raw double use method "getAsDouble" and cannot return a  Optional
        // or use "isPresent()" if the OptionalDOuble is empty
        String message = "the average grade for " + course + " is " + average.orElseThrow(()->new CourseNotFoundException("no students found for that course"))
        +"%";
        model.addAttribute("averageMessage", message);//to String is called here implicitly on message(thymleaf invokes the toString method), but "message" is already a string which concatinated.
        return "AverageGradeResults";// if use a primitive then can use that instead to use this String message in th:text thymleaf it needs to be assigned to a variable in this case "averageMessage"


}

//----------------------------------------------------------
public String searchForStudentServices(){
    return "StudentSearchPage";
}
    

public String studentSearchIdResultServices(int id,Model model) throws StudentIdNotFoundException{

    model.addAttribute("students",studentRepository.findById(id).orElseThrow(()->new StudenIdNotFoundException("student id not found, please try again")));

    return "StudentResults";
}


    
public String searchStudentByAgeRangeServices(String Course,int minAge, int maxAge,int minGrade, int maxGrade, Model model) {
    List<Student> studentList = studentRepository.findAll()
            .stream().filter(obj->obj.getAge() >= minAge && obj.getAge()<=maxAge).
            filter(obj->obj.getGrade()>=minGrade && obj.getGrade()<=maxGrade).
            filter(obj->obj.getCourse().equals(Course)).toList();

    model.addAttribute("students",studentList);
    return "StudentResults";

}
//-------------------------------------------------------

      public String updateStudentPageServices(Model model){
        model.addAttribute("request",new Student());
        return "UpdateStudentPage";

      }

    public <T> T orDefault(T value,T databaseValue){
            return value==null || value.toString().trim().isEmpty()?databaseValue:value;
        }

    public String updateStudentServices(Integer id,String firstName,String surname, Integer age, String course, Integer grade,String username,String local_Date,Model model) throws StudentIdNotFoundException{
        Student student=studentRepository.findById(id).orElseThrow(()->new StudentIdNotFoundException("there was no student found with that id, please try again"));
            student.setFirstName(orDefault(firstName,student.getFirstName()));
            student.setSurname(orDefault(surname,student.getSurname()));
            student.setAge(orDefault(age, student.getAge()));
            student.setCourse(orDefault(course,student.getCourse()));
            student.setGrade(orDefault(grade, student.getGrade()));
            student.setUsername(orDefault(username,student.getUsername()));
            student.setLocalDate(local_Date!=null?LocalDate.parse(local_Date):student.getLocalDate());
            studentRepository.save(student);

            //as we are using the same "id" then we are not creating a new only updating regardless if we use post or patch but by convention patch is better for updating

        return "PageSavedSuccessfully";

        }



































}



































}
