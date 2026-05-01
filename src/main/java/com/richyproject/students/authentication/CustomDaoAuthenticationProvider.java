package com.richyproject.students.authentication;

import com.richyproject.students.model.Employee;
import com.richyproject.students.model.Student;
import com.richyproject.students.repository.EmployeeRepository;
import com.richyproject.students.repository.StudentRepository;
import com.richyproject.students.services.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import java.time.LocalDate;

import java.time.LocalTime;
import java.util.Optional;

public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {


    @Autowired
    StudentRepository studentRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //System.out.println(authentication.getCredentials());
        //System.out.println(authentication.getName());//this has to be the login username surely?, not sure what getCredentials is for.....

        Optional<Student> getStudent = studentRepository.findByUsername(authentication.getName());// this object if found is however from the database where we can get the name
        Optional<Employee> getEmployee = employeeRepository.findByUsername(authentication.getName());
        if (getEmployee.isPresent()) {
        System.out.println("employee name is"+getEmployee.get().getUsername());
        }
    if(!getEmployee.isPresent() && !getStudent.isPresent()) {
            System.out.println("1 exception is thrown here ");
            throw new LockedException("you have entered invalid username please try again");
        }

        LocalTime localTime=LocalTime.now();
        if(getStudent.isPresent() && getStudent.get().getLocalDate()==null){
            System.out.println("2 exception is thrown here ");

            throw new LockedException("no expiry date for user was found");//returns back to the failureUrl method I am assuming after this message is thrown to the terminal
        }


        if(getStudent.isPresent() && getStudent.get().getLocalDate().isBefore(LocalDate.now())){
            System.out.println("3 exception is thrown here ");

            throw new LockedException("your account has expired, please reset your account");//returns back to the failureUrl method I am assuming after this message is thrown to the terminal
        }
        LocalTime currentTime = LocalTime.now();
        LocalTime startTime=LocalTime.of(8,30);
        LocalTime endTime=LocalTime.of(23,00);
        if (currentTime.isBefore(startTime) || currentTime.isAfter(endTime)) {
            System.out.println("4 exception is thrown here ");

            throw new LockedException("Login only allowed during business hours");
            }



        String rawPassword=authentication.getCredentials().toString();//gets the raw password, getCredentials returns a ..... object and using toString gets the raw password


            if (getStudent.isPresent() && !passwordEncoder.matches(rawPassword,getStudent.get().getPassword())) {//this "matches" method is able to compare the raw input password with the encrypted password in the database
                System.out.println("5 exception is thrown here ");

                throw new LockedException("you have entered invalid password please try again");

            }


        return super.authenticate(authentication);//still need to return here just in case a exception does not get thrown


        }















    }


