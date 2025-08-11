package com.richyproject.students.repository;

import com.richyproject.students.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository

public interface StudentRepository extends JpaRepository<Student,Integer> {

    Optional<Student>  findByUsername(String username);








}
