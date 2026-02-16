package com.richyproject.students.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.Base64;

@Service
public class CustomUserEncryptService {


        @Autowired
        PasswordEncoder passwordEncoder;


        public String encodePassword(CharSequence rawPassword){
            return passwordEncoder.encode(rawPassword);


        }









}
