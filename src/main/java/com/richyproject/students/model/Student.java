package com.richyproject.students.model;

import com.richyproject.students.Enum.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.util.EnumMap;

@Data
@Entity


public class Student {



    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    int id;
    private String firstName;
    private String surname;
    private int age;
    private String course;
    private int grade;

    @Column(unique = true)
    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.STUDENT;

    private String profilePicture;









}
