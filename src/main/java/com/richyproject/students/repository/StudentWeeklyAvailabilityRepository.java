package com.richyproject.students.repository;

import com.richyproject.students.model.Student;
import com.richyproject.students.model.StudentWeeklyAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface StudentWeeklyAvailabilityRepository extends JpaRepository<StudentWeeklyAvailability,Long> {
     List<StudentWeeklyAvailability> findBymondayAvailableTrue();
    List<StudentWeeklyAvailability> findBytuesdayAvailableTrue();
    List<StudentWeeklyAvailability> findBywednesdayAvailableTrue();
    List<StudentWeeklyAvailability> findBythursdayAvailableTrue();
    List<StudentWeeklyAvailability> findByfridayAvailableTrue();
    List<StudentWeeklyAvailability> findBysaturdayAvailableTrue();
    List<StudentWeeklyAvailability> findBysundayAvailableTrue();

    void deleteByStudnentId(Integer id);
    // come back to check if works



}
