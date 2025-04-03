package com.example.courseregistrationsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.courseregistrationsystem.model.Admin;
import com.example.courseregistrationsystem.model.Instructor;
import com.example.courseregistrationsystem.model.Student;
import com.example.courseregistrationsystem.service.AdminService;
import com.example.courseregistrationsystem.service.InstructorService;
import com.example.courseregistrationsystem.service.StudentService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private AdminService adminService;

    // Student Login
    @GetMapping("/student/login")
    public String showStudentLogin(HttpSession session) {
        if (session.getAttribute("studentId") != null) {
            return "redirect:/student/dashboard";
        }
        return "student-login";
    }

    @PostMapping("/student/login")
    public String processStudentLogin(@RequestParam String studentId, 
                                    @RequestParam String password,
                                    Model model,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        Student student = studentService.findByStudentId(studentId);
        if (student != null && student.getPassword().equals(password)) {
            session.setAttribute("studentId", studentId);
            redirectAttributes.addFlashAttribute("success", "Welcome back, " + student.getUsername() + "!");
            return "redirect:/student/dashboard";
        }
        model.addAttribute("error", "Invalid credentials");
        return "student-login";
    }

    // Instructor Login
    @GetMapping("/instructor/login")
    public String showInstructorLogin(HttpSession session) {
        if (session.getAttribute("instructorId") != null) {
            return "redirect:/instructor/dashboard";
        }
        return "instructor-login";
    }

    @PostMapping("/instructor/login")
    public String processInstructorLogin(@RequestParam String instructorId,
                                       @RequestParam String password,
                                       Model model,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes) {
        Instructor instructor = instructorService.findByInstructorIdWithCourses(instructorId);
        if (instructor != null && instructor.getPassword().equals(password)) {
            session.setAttribute("instructorId", instructorId);
            redirectAttributes.addFlashAttribute("success", "Welcome back, " + instructor.getUsername() + "!");
            return "redirect:/instructor/dashboard";
        }
        model.addAttribute("error", "Invalid credentials");
        return "instructor-login";
    }

    // Admin Login
    @GetMapping("/admin/login")
    public String showAdminLogin(HttpSession session) {
        if (session.getAttribute("adminId") != null) {
            return "redirect:/admin/dashboard";
        }
        return "admin-login";
    }

    @PostMapping("/admin/login")
    public String processAdminLogin(@RequestParam String adminId,
                                  @RequestParam String password,
                                  Model model,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        Admin admin = adminService.findByAdminId(adminId);
        if (admin != null && admin.getPassword().equals(password)) {
            session.setAttribute("adminId", adminId);
            redirectAttributes.addFlashAttribute("success", "Welcome back, " + admin.getUsername() + "!");
            return "redirect:/admin/dashboard";
        }
        model.addAttribute("error", "Invalid credentials");
        return "admin-login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("success", "You have been logged out successfully.");
        return "redirect:/";
    }
} 