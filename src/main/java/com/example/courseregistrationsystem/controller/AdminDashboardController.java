package com.example.courseregistrationsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.courseregistrationsystem.model.Admin;
import com.example.courseregistrationsystem.model.Course;
import com.example.courseregistrationsystem.model.Instructor;
import com.example.courseregistrationsystem.service.AdminService;
import com.example.courseregistrationsystem.service.CourseService;
import com.example.courseregistrationsystem.service.InstructorService;
import com.example.courseregistrationsystem.service.StudentService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String adminId = (String) session.getAttribute("adminId");
        
        if (adminId == null) {
            redirectAttributes.addFlashAttribute("error", "Please login to access the dashboard");
            return "redirect:/admin/login";
        }

        Admin admin = adminService.findByAdminId(adminId);
        if (admin == null) {
            redirectAttributes.addFlashAttribute("error", "Admin not found");
            return "redirect:/admin/login";
        }

        // Add admin information
        model.addAttribute("admin", admin);

        // Add system statistics
        model.addAttribute("totalStudents", studentService.getAllStudents().size());
        model.addAttribute("totalInstructors", instructorService.getAllInstructors().size());
        model.addAttribute("totalCourses", courseService.getAllCourses().size());

        // Add lists for management
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("instructors", instructorService.getAllInstructors());
        model.addAttribute("courses", courseService.getAllCourses());

        return "admin-dashboard";
    }

    @PostMapping("/courses/add")
    public String addCourse(@RequestParam String courseCode,
                          @RequestParam String courseName,
                          @RequestParam Integer credits,
                          @RequestParam Integer capacity,
                          @RequestParam(required = false) Long instructorId,
                          RedirectAttributes redirectAttributes) {
        try {
            Course course = new Course();
            course.setCourseCode(courseCode);
            course.setCourseName(courseName);
            course.setCredits(credits);
            course.setCapacity(capacity);

            if (instructorId != null) {
                Instructor instructor = instructorService.getInstructorById(instructorId);
                if (instructor != null) {
                    course.setInstructor(instructor);
                }
            }

            courseService.createCourse(course);
            redirectAttributes.addFlashAttribute("success", "Course added successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add course: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/courses/delete")
    public String deleteCourse(@RequestParam Long courseId, RedirectAttributes redirectAttributes) {
        try {
            courseService.deleteCourse(courseId);
            redirectAttributes.addFlashAttribute("success", "Course deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete course: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }
} 