package com.richyproject.students.repository;

import com.richyproject.students.model.Student;
import com.richyproject.students.model.StudentWeeklyAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface StudentWeeklyAvailabilityRepository extends JpaRepository<StudentWeeklyAvailability,Long> {

    // come back to check if works



}
