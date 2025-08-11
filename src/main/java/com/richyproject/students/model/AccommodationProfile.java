package com.richyproject.students.model;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.persistence.Id;  //  CORRECT - jakarta is for JPA

import java.util.List;

@Entity
@Data

public class AccommodationProfile {


    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private int id;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name="smoker")
    private String smoker;
    private double minBudget;
    private double maxBudget;
    private String hasPets;
    private String myGender;
    private String dietaryPattern;
    private String guests;
    private int age;
    private String email;
    private String course;









}
