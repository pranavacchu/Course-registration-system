package com.example.courseregistrationsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.courseregistrationsystem.model.Student;
import com.example.courseregistrationsystem.service.CourseService;
import com.example.courseregistrationsystem.service.StudentService;

import jakarta.servlet.http.HttpSession;

@Controller
public class StudentDashboardController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/student/dashboard")
    public String showDashboard(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String studentId = (String) session.getAttribute("studentId");
        if (studentId == null) {
            redirectAttributes.addFlashAttribute("error", "Please login first");
            return "redirect:/student/login";
        }
        
        Student student = studentService.findByStudentIdWithCourses(studentId);
        if (student == null) {
            session.invalidate();
            redirectAttributes.addFlashAttribute("error", "Student not found");
            return "redirect:/student/login";
        }
        
        model.addAttribute("student", student);
        model.addAttribute("availableCourses", courseService.getAvailableCourses());
        return "student-dashboard";
    }

    @PostMapping("/student/courses/enroll")
    public String enrollCourse(@RequestParam Long courseId,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        String studentId = (String) session.getAttribute("studentId");
        if (studentId == null) {
            redirectAttributes.addFlashAttribute("error", "Please login first");
            return "redirect:/student/login";
        }

        try {
            studentService.enrollInCourse(studentId, courseId);
            redirectAttributes.addFlashAttribute("success", "Successfully enrolled in the course!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/student/dashboard";
    }

    @PostMapping("/student/courses/drop")
    public String dropCourse(@RequestParam Long courseId,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        String studentId = (String) session.getAttribute("studentId");
        if (studentId == null) {
            redirectAttributes.addFlashAttribute("error", "Please login first");
            return "redirect:/student/login";
        }

        try {
            studentService.dropCourse(studentId, courseId);
            redirectAttributes.addFlashAttribute("success", "Successfully dropped the course!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/student/dashboard";
    }
} 