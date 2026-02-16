package com.richyproject.students.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class StudentWeeklyAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String course;

    // Monday fields
    private String monday_available;
    private String monday_start1;
    private String monday_end1;
    private String monday_start2;
    private String monday_end2;
    private String monday_start3;
    private String monday_end3;

    // Tuesday fields
    private String tuesday_available;
    private String tuesday_start1;
    private String tuesday_end1;
    private String tuesday_start2;
    private String tuesday_end2;
    private String tuesday_start3;
    private String tuesday_end3;

    // Wednesday fields
    private String wednesday_available;
    private String wednesday_start1;
    private String wednesday_end1;
    private String wednesday_start2;
    private String wednesday_end2;
    private String wednesday_start3;
    private String wednesday_end3;

    // Thursday fields
    private String thursday_available;
    private String thursday_start1;
    private String thursday_end1;
    private String thursday_start2;
    private String thursday_end2;
    private String thursday_start3;
    private String thursday_end3;

    // Friday fields
    private String friday_available;
    private String friday_start1;
    private String friday_end1;
    private String friday_start2;
    private String friday_end2;
    private String friday_start3;
    private String friday_end3;

    // Saturday fields
    private String saturday_available;
    private String saturday_start1;
    private String saturday_end1;
    private String saturday_start2;
    private String saturday_end2;
    private String saturday_start3;
    private String saturday_end3;

    // Sunday fields
    private String sunday_available;
    private String sunday_start1;
    private String sunday_end1;
    private String sunday_start2;
    private String sunday_end2;
    private String sunday_start3;
    private String sunday_end3;

    // Link to student
    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;


}
