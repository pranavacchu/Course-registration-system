package com.example.courseregistrationsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.example.courseregistrationsystem.model.Admin;
import com.example.courseregistrationsystem.model.Course;
import com.example.courseregistrationsystem.model.Instructor;
import com.example.courseregistrationsystem.model.Student;
import com.example.courseregistrationsystem.service.AdminService;
import com.example.courseregistrationsystem.service.CourseService;
import com.example.courseregistrationsystem.service.InstructorService;
import com.example.courseregistrationsystem.service.StudentService;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private static final Logger logger = LoggerFactory.getLogger(AdminDashboardController.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/dashboard")
    @Transactional(readOnly = true)
    public String showDashboard(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        logger.info("Accessing admin dashboard");
        
        String adminId = (String) session.getAttribute("adminId");
        if (adminId == null) {
            logger.warn("Attempt to access dashboard without admin login");
            redirectAttributes.addFlashAttribute("error", "Please login to access the dashboard");
            return "redirect:/admin/login";
        }

        try {
            Admin admin = adminService.findByAdminId(adminId);
            if (admin == null) {
                logger.error("Admin not found for ID: {}", adminId);
                redirectAttributes.addFlashAttribute("error", "Admin not found");
                return "redirect:/admin/login";
            }

            logger.info("Loading dashboard data for admin: {}", adminId);
            
            // Add admin information
            model.addAttribute("admin", admin);

            // Get all data with proper relationships
            List<Student> students = studentService.getAllStudents();
            List<Instructor> instructors = instructorService.getAllInstructors();
            List<Course> courses = courseService.getAllCoursesWithRelationships();
            
            // Calculate statistics
            int totalStudents = students != null ? students.size() : 0;
            int totalInstructors = instructors != null ? instructors.size() : 0;
            int totalCourses = courses != null ? courses.size() : 0;
            
            // Add data to model
            model.addAttribute("totalStudents", totalStudents);
            model.addAttribute("totalInstructors", totalInstructors);
            model.addAttribute("totalCourses", totalCourses);
            model.addAttribute("students", students != null ? students : new ArrayList<>());
            model.addAttribute("instructors", instructors != null ? instructors : new ArrayList<>());
            model.addAttribute("courses", courses != null ? courses : new ArrayList<>());

            logger.info("Dashboard data loaded successfully");
            return "admin-dashboard";
        } catch (Exception e) {
            logger.error("Error loading admin dashboard", e);
            model.addAttribute("error", "An error occurred while loading the dashboard");
            return "admin-dashboard";
        }
    }

    @PostMapping("/courses/add")
    public String addCourse(@RequestParam String courseCode,
                          @RequestParam String courseName,
                          @RequestParam Integer credits,
                          @RequestParam Integer capacity,
                          @RequestParam(required = false) Long instructorId,
                          RedirectAttributes redirectAttributes) {
        logger.info("Adding new course: {}", courseCode);
        try {
            Course course = new Course();
            course.setCourseCode(courseCode);
            course.setCourseName(courseName);
            course.setCredits(credits);
            course.setCapacity(capacity);

            if (instructorId != null) {
                Instructor instructor = instructorService.getInstructorById(instructorId)
                    .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));
                course.setInstructor(instructor);
            }

            courseService.createCourse(course);
            logger.info("Course added successfully: {}", courseCode);
            redirectAttributes.addFlashAttribute("success", "Course added successfully");
        } catch (Exception e) {
            logger.error("Failed to add course: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Failed to add course: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/courses/edit")
    public String editCourse(@RequestParam Long courseId,
                           @RequestParam String courseCode,
                           @RequestParam String courseName,
                           @RequestParam Integer credits,
                           @RequestParam Integer capacity,
                           @RequestParam(required = false) Long instructorId,
                           RedirectAttributes redirectAttributes) {
        logger.info("Editing course with ID: {}", courseId);
        try {
            Course course = courseService.getCourseById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

            course.setCourseCode(courseCode);
            course.setCourseName(courseName);
            course.setCredits(credits);
            course.setCapacity(capacity);

            if (instructorId != null) {
                Instructor instructor = instructorService.getInstructorById(instructorId)
                    .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));
                course.setInstructor(instructor);
            } else {
                course.setInstructor(null);
            }

            courseService.updateCourse(course);
            logger.info("Course updated successfully: {}", courseCode);
            redirectAttributes.addFlashAttribute("success", "Course updated successfully");
        } catch (Exception e) {
            logger.error("Failed to update course: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Failed to update course: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/courses/assign-instructor")
    public String assignInstructorToCourse(@RequestParam Long courseId,
                                         @RequestParam Long instructorId,
                                         RedirectAttributes redirectAttributes) {
        logger.info("Assigning instructor {} to course {}", instructorId, courseId);
        try {
            Course course = courseService.getCourseById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
            
            Instructor instructor = instructorService.getInstructorById(instructorId)
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));
            
            course.setInstructor(instructor);
            courseService.updateCourse(course);
            
            logger.info("Successfully assigned instructor {} to course {}", instructorId, courseId);
            redirectAttributes.addFlashAttribute("success", "Instructor assigned to course successfully");
        } catch (Exception e) {
            logger.error("Error assigning instructor to course: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Failed to assign instructor: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/courses/delete")
    public String deleteCourse(@RequestParam Long courseId, RedirectAttributes redirectAttributes) {
        logger.info("Deleting course with ID: {}", courseId);
        try {
            courseService.deleteCourse(courseId);
            logger.info("Course deleted successfully: {}", courseId);
            redirectAttributes.addFlashAttribute("success", "Course deleted successfully");
        } catch (Exception e) {
            logger.error("Failed to delete course: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Failed to delete course: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }
} 