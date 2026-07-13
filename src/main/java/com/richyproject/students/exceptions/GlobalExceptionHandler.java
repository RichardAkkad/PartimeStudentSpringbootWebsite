package com.richyproject.students.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CourseNotFoundException.class)
    public String handleCourseNotFound(CourseNotFoundException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "ErrorPage";
    }

    @ExceptionHandler(StudentIdNotFoundException.class)
    public String handleStudentNotFound(StudentIdNotFoundException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "ErrorPage";
    }

    @ExceptionHandler(StudentNameNotFoundException.class)
    public String handleStudentNameNotFound(StudentNameNotFoundException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "ErrorPage";
    }



}
