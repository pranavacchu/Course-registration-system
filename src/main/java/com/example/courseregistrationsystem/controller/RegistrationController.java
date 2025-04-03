package com.example.courseregistrationsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.courseregistrationsystem.model.Instructor;
import com.example.courseregistrationsystem.model.Student;
import com.example.courseregistrationsystem.model.User;
import com.example.courseregistrationsystem.service.InstructorService;
import com.example.courseregistrationsystem.service.StudentService;

@Controller
public class RegistrationController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private InstructorService instructorService;

    // Student Registration
    @GetMapping("/student/register")
    public String showStudentRegistration(Model model) {
        model.addAttribute("student", new Student());
        return "student-registration";
    }

    @PostMapping("/student/register")
    public String registerStudent(@ModelAttribute Student student,
                                @RequestParam String confirmPassword,
                                RedirectAttributes redirectAttributes) {
        try {
            if (!student.getPassword().equals(confirmPassword)) {
                throw new RuntimeException("Passwords do not match");
            }
            
            student.setRole(User.UserRole.STUDENT);
            Student savedStudent = studentService.createStudent(student);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/student/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/student/register";
        }
    }

    // Instructor Registration
    @GetMapping("/instructor/register")
    public String showInstructorRegistration(Model model) {
        model.addAttribute("instructor", new Instructor());
        return "instructor-registration";
    }

    @PostMapping("/instructor/register")
    public String registerInstructor(@ModelAttribute Instructor instructor,
                                   @RequestParam String confirmPassword,
                                   RedirectAttributes redirectAttributes) {
        try {
            if (!instructor.getPassword().equals(confirmPassword)) {
                throw new RuntimeException("Passwords do not match");
            }
            
            instructor.setRole(User.UserRole.INSTRUCTOR);
            Instructor savedInstructor = instructorService.createInstructor(instructor);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/instructor/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/instructor/register";
        }
    }
} 