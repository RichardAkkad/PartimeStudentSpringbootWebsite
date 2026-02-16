package com.richyproject.students.model;

import com.richyproject.students.Enum.Role;
import jakarta.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "Employee")
public class Employee {

    //public User(){

    //}

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    int id;
    private String username;
    private String password;
    private String firstName;
    private String surname;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String department;








}
