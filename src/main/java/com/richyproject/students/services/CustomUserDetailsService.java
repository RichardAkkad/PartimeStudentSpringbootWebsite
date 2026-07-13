package com.richyproject.students.services;

import com.richyproject.students.Enum.Role;
import com.richyproject.students.model.Employee;
import com.richyproject.students.model.Student;
import com.richyproject.students.repository.EmployeeRepository;
import com.richyproject.students.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    EmployeeRepository employeeRepository;




    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        System.out.println("Trying to find username: '" + name + "'");



       try {
           Optional<Student> student = studentRepository.findByUsername(name);//returns a optional student object not a optional string object as a particular name(just as a reminder) and also ofNullable is used implicitly
           Optional<Employee> employee = employeeRepository.findByUsername(name);
           System.out.println("Student found: " + student.isPresent());
           System.out.println("Employee found: " + employee.isPresent());


           if (student.isEmpty() && employee.isEmpty()) {
               System.out.println("Both student and employee are empty for username: " + name);

               throw new UsernameNotFoundException("username not found, try again");
           }

           if (student.isPresent()) {
               Student studentOpt = student.get();
               System.out.println("Student role: " + studentOpt.getRole());
               System.out.println("Student password starts with $2a$: " + studentOpt.getPassword().startsWith("$2a$"));


               return User.builder().
                       username(student.get().
                               getUsername()).password(student.get()
                               .getPassword())
                                .roles(Role.STUDENT.toString())
                                .build();


//note that any information returned from the UserDetails is information like username, password and role here , this UserDetails object cant have fields like age,id... etc are not accepted as its not related to security
           } else if (employee.isPresent()) {
               return User.builder()
                       .username(employee.get().getUsername())
                       .password(employee.get().getPassword())
                       .roles(Role.TEACHER.toString())
                       .build();//could just do "Role.TEACHER.toString()/name()" or directly place the String "TEACHER" as the parameter takes a string. we could just pass in a string directly so pointless having a Role enum


           } else {
               throw new UsernameNotFoundException("username not found, try again");

           }
       }
          catch (Exception e){
            System.out.println("Exception during repository call: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }






    }






    }
