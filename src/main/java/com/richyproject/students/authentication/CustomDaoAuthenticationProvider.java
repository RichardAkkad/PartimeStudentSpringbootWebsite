package com.richyproject.students.authentication;

import com.richyproject.students.model.Student;
import com.richyproject.students.repository.StudentRepository;
import com.richyproject.students.services.CustomUserDetailsService;
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
    PasswordEncoder passwordEncoder;



    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        Optional<Student> getStudent=studentRepository.findByUsername(authentication.getName());
        LocalTime localTime=LocalTime.now();
        if(getStudent.isPresent()&& getStudent.get().getLocalDate()==null){

            throw new LockedException("no expiry date for user was found");//returns back to the failureUrl method I am assuming after this message is thrown to the terminal
        }
        if(getStudent.isPresent() && getStudent.get().getLocalDate().isBefore(LocalDate.now())){

            throw new LockedException("your account has expired, please reset your account");//returns back to the failureUrl method I am assuming after this message is thrown to the terminal
        }
        LocalTime currentTime = LocalTime.now();
        LocalTime startTime=LocalTime.of(9,15);
        LocalTime endTime=LocalTime.of(17,30);
        if (currentTime.isBefore(startTime) || currentTime.isAfter(endTime)) {
            throw new LockedException("Login only allowed during business hours");
            }

        if(getStudent.isEmpty()){

            throw new LockedException("you have entered invalid username please try again");

        }
        String rawPassword=authentication.getCredentials().toString();
        String  encryptedPassword=getStudent.get().getPassword();
        if (getStudent.isPresent() && !passwordEncoder.matches(rawPassword,encryptedPassword)) {
            throw new LockedException("you have entered invalid password please try again");

        }

        return super.authenticate(authentication);//still need to return here just in case a exception does not get thrown


        }















    }


