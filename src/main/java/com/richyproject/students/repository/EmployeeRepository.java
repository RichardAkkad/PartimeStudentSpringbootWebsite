package com.richyproject.students.repository;

import com.richyproject.students.model.Employee;
import com.richyproject.students.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Integer> {

    Optional<Employee> findByUsername(String username);



}
