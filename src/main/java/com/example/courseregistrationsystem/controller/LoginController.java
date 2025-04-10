package com.example.courseregistrationsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.courseregistrationsystem.model.Admin;
import com.example.courseregistrationsystem.model.Instructor;
import com.example.courseregistrationsystem.model.Student;
import com.example.courseregistrationsystem.service.AdminService;
import com.example.courseregistrationsystem.service.InstructorService;
import com.example.courseregistrationsystem.service.StudentService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private StudentService studentService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private InstructorService instructorService;

    @GetMapping("/")
    public String home(HttpSession session) {
        // Check if any user is logged in and redirect accordingly
        if (session.getAttribute("studentId") != null) {
            return "redirect:/student/dashboard";
        } else if (session.getAttribute("instructorId") != null) {
            return "redirect:/instructor/dashboard";
        } else if (session.getAttribute("adminId") != null) {
            return "redirect:/admin/dashboard";
        }
        return "index"; // Return to home page if no one is logged in
    }

    // Student Login
    @GetMapping("/student/login")
    public String showStudentLogin(HttpSession session) {
        // If already logged in as student, redirect to dashboard
        if (session.getAttribute("studentId") != null) {
            return "redirect:/student/dashboard";
        }
        // If logged in as someone else, logout first
        if (session.getAttribute("instructorId") != null || session.getAttribute("adminId") != null) {
            session.invalidate();
        }
        return "student-login";
    }

    @PostMapping("/student/login")
    public String processStudentLogin(@RequestParam String studentId, 
                                    @RequestParam String password,
                                    Model model,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        logger.info("Attempting student login for ID: {}", studentId);
        try {
            Student student = studentService.findByStudentId(studentId);
            if (student.getPassword().equals(password)) {
                logger.info("Student login successful for: {}", studentId);
                session.setAttribute("studentId", studentId);
                redirectAttributes.addFlashAttribute("success", "Welcome back, " + student.getUsername() + "!");
                return "redirect:/student/dashboard";
            }
            logger.warn("Student login failed for ID: {} - Invalid password", studentId);
            model.addAttribute("error", "Invalid password");
            return "student-login";
        } catch (RuntimeException e) {
            logger.error("Student not found: {}", e.getMessage());
            model.addAttribute("error", "Student ID not found");
            return "student-login";
        } catch (Exception e) {
            logger.error("Unexpected error during student login: {}", e.getMessage(), e);
            model.addAttribute("error", "An error occurred during login. Please try again.");
            return "student-login";
        }
    }

    // Instructor Login
    @GetMapping("/instructor/login")
    public String showInstructorLogin(HttpSession session) {
        // If already logged in as instructor, redirect to dashboard
        if (session.getAttribute("instructorId") != null) {
            return "redirect:/instructor/dashboard";
        }
        // If logged in as someone else, logout first
        if (session.getAttribute("studentId") != null || session.getAttribute("adminId") != null) {
            session.invalidate();
        }
        return "instructor-login";
    }

    @PostMapping("/instructor/login")
    public String processInstructorLogin(@RequestParam String email,
                                       @RequestParam String password,
                                       Model model,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes) {
        logger.info("Attempting instructor login for email: {}", email);
        try {
            Instructor instructor = instructorService.findByEmail(email);
            if (instructor.getPassword().equals(password)) {
                logger.info("Instructor login successful for: {}", email);
                session.setAttribute("instructorId", instructor.getInstructorId());
                redirectAttributes.addFlashAttribute("success", "Welcome back, " + instructor.getUsername() + "!");
                return "redirect:/instructor/dashboard";
            }
            logger.warn("Instructor login failed for email: {} - Invalid password", email);
            model.addAttribute("error", "Invalid password");
            return "instructor-login";
        } catch (RuntimeException e) {
            logger.error("Instructor not found: {}", e.getMessage());
            model.addAttribute("error", "Email not found");
            return "instructor-login";
        } catch (Exception e) {
            logger.error("Unexpected error during instructor login: {}", e.getMessage(), e);
            model.addAttribute("error", "An error occurred during login. Please try again.");
            return "instructor-login";
        }
    }

    // Admin Login
    @GetMapping("/admin/login")
    public String showAdminLogin(HttpSession session) {
        // If already logged in as admin, redirect to dashboard
        if (session.getAttribute("adminId") != null) {
            return "redirect:/admin/dashboard";
        }
        // If logged in as someone else, logout first
        if (session.getAttribute("studentId") != null || session.getAttribute("instructorId") != null) {
            session.invalidate();
        }
        return "admin-login";
    }

    @PostMapping("/admin/login")
    public String processAdminLogin(@RequestParam String adminId,
                                  @RequestParam String password,
                                  Model model,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        logger.info("Attempting admin login for ID: {}", adminId);
        try {
            Admin admin = adminService.findByAdminId(adminId);
            if (admin == null) {
                logger.warn("Admin login failed for ID: {} - Admin not found", adminId);
                model.addAttribute("error", "Admin ID not found");
                return "admin-login";
            }

            if (admin.getPassword().equals(password)) {
                logger.info("Admin login successful for: {}", adminId);
                session.setAttribute("adminId", adminId);
                redirectAttributes.addFlashAttribute("success", "Welcome back, " + admin.getUsername() + "!");
                return "redirect:/admin/dashboard";
            }
            
            logger.warn("Admin login failed for ID: {} - Invalid password", adminId);
            model.addAttribute("error", "Invalid password");
            return "admin-login";
        } catch (Exception e) {
            logger.error("Unexpected error during admin login: {}", e.getMessage(), e);
            model.addAttribute("error", "An error occurred during login. Please try again.");
            return "admin-login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("success", "You have been logged out successfully.");
        return "redirect:/";
    }
} 