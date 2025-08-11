package com.richyproject.students.services;


import com.richyproject.students.Enum.Role;
import com.richyproject.students.exceptions.StudentIdNotFoundException;
import com.richyproject.students.exceptions.StudentNameNotFoundException;
import com.richyproject.students.exceptions.ZeroException;
import com.richyproject.students.model.Student;
import com.richyproject.students.repository.AccommodationProfileRepository;
import com.richyproject.students.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.File;
import java.util.*;
import java.util.function.Function;

@Service
public class StudentService {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    AccommodationProfileRepository accommodationProfileRepository;

    @Autowired
    CustomUserEncryptService customUserEncryptService;




    public String addStudentServices(){
        return "AddStudent";

    }


    public String saveStudentServices(Student student) throws Exception{
        String encoded=customUserEncryptService.encodePassword(student.getPassword());//calls my encodePassword method (not a implementation)that contains the "encode" method( where the bencryptpasswordencoder interface implements the passwordencoder class, thats why we create a bencrypt....... bean so we can call the encode method)
        student.setPassword(encoded);
        student.setRole(Role.STUDENT);
            studentRepository.save(student);
            return "PageSavedSuccessfully";


    }




    public String studentServicesDeletePage(){
        return "DeleteStudentPage";
    }



    public String deleteActualStudent(Integer id) throws StudentNameNotFoundException {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isEmpty()) {
            throw new StudentNameNotFoundException("username not found, try again");
        }
        else {
            Student student = optionalStudent.get();

            // Get the picture filename BEFORE deleting the student
            String profilePicture = student.getProfilePicture();

            // Delete student from database
            studentRepository.deleteById(id);

            // Delete the picture file if it exists
            if (profilePicture != null && !profilePicture.isEmpty()) {
                deleteProfilePictureFile(profilePicture);
            }

            return "StudentDeletedSuccessfully";
        }
    }

    private void deleteProfilePictureFile(String filename) {
        try {
            String uploadDir = "C:/Users/richa/OneDrive/Desktop/student-images/";
            File fileToDelete = new File(uploadDir + filename);

            if (fileToDelete.exists()) {
                boolean deleted = fileToDelete.delete();
                System.out.println("Picture file deleted from service: " + deleted + " - " + filename);
            }
        } catch (Exception e) {
            System.out.println("Error deleting picture from service: " + e.getMessage());
        }
    }












    public String findAgefromServices(int age, Model model) {
        List<Student> li = studentRepository.findAll();

        Function<Student, String> lambVar = student -> String.valueOf(student.getId());

        List<String> info = li.stream().filter(student  -> student.getAge() < age).map(Student::getId).map(String::valueOf).toList();


        String message="the following id's are for the student's whos age is " + age + " and below is..... "+ String.join(", ",info);

        model.addAttribute("ageMessage", message);


        return "AgeRangeResults";


    }













    public String getAgeRangePercentageResultsServices(int age, Model model){

        List<Student> li=studentRepository.findAll();


        List<Integer> ageLi =new ArrayList<>();
        li.stream().filter(student->student.getAge()<=age).forEach(student->ageLi.add(student.getAge()));


        String message= ageLi.isEmpty()?"unfortunately no students of that age "+age+" or less were found":(ageLi.size()*100)/ li.size()+"% of the students are under the age of "+age;
        //dont think there is a point having a ternary sentence here if (inside a try block as not possible to have a arithmetic exception), because if its empty which
        //means ageLi.isEmpty size is zero so that takes care of the 0, therefore "ageLi.size()*100)/li.size()" cannot be "0/li.size()"


        model.addAttribute("agePercentageMessage", message);//basicly "message" is assigned to the variable "agePercentageMessage", also wont need to convert it into a toString as its already a
        return "AgeRangePercentageResults";//String which is what thymleaf would do if its not a String, it would use toString on it.


    }












    public String averageGradeResults(String course,Model model)throws ZeroException {

        List<Student> li=studentRepository.findAll();


        OptionalDouble average=li.stream().filter(obj->obj.getCourse().equals(course)).mapToInt(Student::getGrade).average();

        if(average.isEmpty()) {
            throw new ZeroException("cant divide by zero");
        }


        //"average" returns a OptionalDouble, but not a raw Double , if you want the raw double use method "getAsDouble" and cannot return a  Optional
        // or use "isPresent()" if the OptionalDOuble is empty
        String message = "the average grade for " + course + " is " + average.getAsDouble()+"%";
        model.addAttribute("averageMessage", message.toString());//to String is called here implicitly on message(thymleaf invokes the toString method), but "message" is already a string which concatinated.
        return "AverageGradeResults";// if use a primitive then can use that instead to use this String message in th:text thymleaf it needs to be assigned to a variable in this case "averageMessage"




}






//----------------------------------------------------------
public String searchForStudentServices(){
    return "StudentSearchPage";
}

public String studentSearchIdResultServices(int id,Model model) throws StudentNameNotFoundException{

    Optional<Student> optionalStudent =studentRepository.findById(id);
    model.addAttribute("student",optionalStudent.get());

    if(optionalStudent.isEmpty()){
        throw new StudentNameNotFoundException("username not found, try again");
    }
    List<Student> result = Arrays.asList(optionalStudent.get());
    model.addAttribute("students", result);


    return "StudentResults";
}
public String searchStudentByAgeRangeServices(String Course,int MinAge, int MaxAge,int MinGrade, int MaxGrade, Model model) {
    List<Student> studentList = studentRepository.findAll()
            .stream().filter(obj->obj.getAge() >= MinAge && obj.getAge()<=MaxAge).
            filter(obj->obj.getGrade()>=MinGrade && obj.getGrade()<=MaxGrade).
            filter(obj->obj.getCourse().equals(Course)).toList();

    model.addAttribute("students",studentList);
    return "StudentResults";

}
//-------------------------------------------------------






      public String updateStudentPageServices(Model model){
        model.addAttribute("request",new Student());
        return "UpdateStudentPage";

      }
    public String updateStudentServices(Integer id,String firstName,String surname, Integer age, String course, Integer grade,String username,Model model) throws StudentIdNotFoundException{
        Optional<Student> optionalStudent=studentRepository.findById(id);
        if(optionalStudent.isPresent()){
            optionalStudent.get().setId(id!=null?id:optionalStudent.get().getId());
            optionalStudent.get().setFirstName(firstName!=null?firstName:optionalStudent.get().getFirstName());
            optionalStudent.get().setSurname(surname!=null?surname:optionalStudent.get().getSurname());
            optionalStudent.get().setAge(age!=null?age:optionalStudent.get().getAge());
            optionalStudent.get().setCourse(course!=null?course:optionalStudent.get().getCourse());
            optionalStudent.get().setGrade(grade!=null?grade:optionalStudent.get().getGrade());
            optionalStudent.get().setUsername(username!=null?username:optionalStudent.get().getUsername());
            studentRepository.save(optionalStudent.get());

        }

        else {
            throw new StudentIdNotFoundException("there was no student found with that id, please try again");
        }
        return "PageSavedSuccessfully";

        }



































}