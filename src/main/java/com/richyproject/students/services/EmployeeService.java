package com.richyproject.students.services;


import com.richyproject.students.Enum.Role;
import com.richyproject.students.exceptions.EmployeeNameNotFoundException;
import com.richyproject.students.model.Employee;
import com.richyproject.students.repository.EmployeeRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Optional;

@Service//just states that bean is needed for this class that is related to services, and it will be used to call methods of this class. might be a bean that implements a built in eg UserDetailsService which has only the
public class EmployeeService {//loadByUsername method, like the CustomUserDetailsService class, or might not implement a built in class just like this class which does not implement a built in class
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    CustomUserEncryptService customUserEncryptService;


    public String addEmployeeServices(Model model){
        model.addAttribute("request",new Employee());
        return "AddNewEmployee";
    }

    public String saveEmployeeServices(Employee employee){
        String encoded =customUserEncryptService.encodePassword(employee.getPassword());
        employee.setPassword(encoded);
        employee.setRole(Role.TEACHER);
        employeeRepository.save(employee);


        return "PageSavedSuccessfully";

    }




    public String deleteEmployeeServices(){

        return "DeleteEmployeePage";

    }




    public String deleteActualEmployeeServices(int id) throws EmployeeNameNotFoundException{
        Optional<Employee> optionalEmployee= employeeRepository.findById(id);
        if (optionalEmployee.isPresent()){
            employeeRepository.delete(optionalEmployee.get());
        }
        else{

            throw new EmployeeNameNotFoundException("this id does not exist");

        }
        return "StudentDeletedSuccessfully";

    }











}
