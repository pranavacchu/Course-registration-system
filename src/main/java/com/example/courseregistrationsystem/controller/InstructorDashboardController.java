package com.example.courseregistrationsystem.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.courseregistrationsystem.model.Course;
import com.example.courseregistrationsystem.model.CourseContent;
import com.example.courseregistrationsystem.model.Grade;
import com.example.courseregistrationsystem.model.Instructor;
import com.example.courseregistrationsystem.model.Student;
import com.example.courseregistrationsystem.service.CourseContentService;
import com.example.courseregistrationsystem.service.CourseService;
import com.example.courseregistrationsystem.service.GradeService;
import com.example.courseregistrationsystem.service.InstructorService;
import com.example.courseregistrationsystem.service.StudentService;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/instructor")
public class InstructorDashboardController {
    private static final Logger logger = LoggerFactory.getLogger(InstructorDashboardController.class);
    
    private final CourseService courseService;
    private final GradeService gradeService;
    private final CourseContentService courseContentService;
    private final InstructorService instructorService;
    private final StudentService studentService;

    @Autowired
    public InstructorDashboardController(CourseService courseService, 
                                       GradeService gradeService,
                                       CourseContentService courseContentService,
                                       InstructorService instructorService,
                                       StudentService studentService) {
        this.courseService = courseService;
        this.gradeService = gradeService;
        this.courseContentService = courseContentService;
        this.instructorService = instructorService;
        this.studentService = studentService;
    }
    
    @GetMapping("/dashboard")
    @Transactional(readOnly = true)
    public String showDashboard(Model model, HttpSession session) {
        String instructorId = (String) session.getAttribute("instructorId");
        if (instructorId == null) {
            return "redirect:/instructor/login";
        }

        try {
            Instructor instructor = instructorService.findByInstructorId(instructorId);
            if (instructor == null) {
                session.invalidate();
                return "redirect:/instructor/login";
            }

            // Get courses with enrolled students
            List<Course> courses = courseService.findByInstructorWithEnrolledStudents(instructor);
            
            // Calculate statistics
            int totalStudents = courses.stream()
                .mapToInt(course -> course.getEnrolledStudents().size())
                .sum();
            
            double averageClassSize = courses.isEmpty() ? 0.0 : 
                (double) totalStudents / courses.size();
            
            // Add data to model
            model.addAttribute("instructor", instructor);
            model.addAttribute("courses", courses);
            model.addAttribute("totalStudents", totalStudents);
            model.addAttribute("averageClassSize", String.format("%.1f", averageClassSize));
            
            return "instructor-dashboard";
        } catch (Exception e) {
            logger.error("Error loading instructor dashboard", e);
            model.addAttribute("error", "An error occurred while loading the dashboard");
            return "instructor-dashboard";
        }
    }
    
    @PostMapping("/courses/content/add")
    @Transactional
    public String addCourseContent(@RequestParam Long courseId, 
                                 @RequestParam String title,
                                 @RequestParam String description,
                                 @RequestParam String contentType,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        String instructorId = (String) session.getAttribute("instructorId");
        if (instructorId == null) {
            return "redirect:/instructor/login";
        }
        
        Instructor instructor = instructorService.findByInstructorId(instructorId);
        if (instructor == null) {
            session.invalidate();
            return "redirect:/instructor/login";
        }
        
        Course course = courseService.getCourseById(courseId)
            .orElseThrow(() -> new IllegalArgumentException("Course not found"));
            
        // Check if instructor owns the course
        if (!course.getInstructor().getId().equals(instructor.getId())) {
            redirectAttributes.addFlashAttribute("error", "You don't have permission to add content to this course");
            return "redirect:/instructor/dashboard";
        }
        
        CourseContent content = new CourseContent();
        content.setTitle(title);
        content.setDescription(description);
        content.setContentType(CourseContent.ContentType.valueOf(contentType));
        content.setCourse(course);
        
        courseContentService.createCourseContent(content);
        
        redirectAttributes.addFlashAttribute("success", "Course content added successfully");
        return "redirect:/instructor/dashboard";
    }
    
    @PostMapping("/course/{courseId}/grade")
    @Transactional
    public String gradeStudent(@PathVariable Long courseId,
                             @RequestParam Long studentId,
                             @RequestParam Double score,
                             @RequestParam String comments,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        String instructorId = (String) session.getAttribute("instructorId");
        if (instructorId == null) {
            return "redirect:/instructor/login";
        }
        
        Instructor instructor = instructorService.findByInstructorId(instructorId);
        if (instructor == null) {
            session.invalidate();
            return "redirect:/instructor/login";
        }
        
        Course course = courseService.getCourseById(courseId)
            .orElseThrow(() -> new IllegalArgumentException("Course not found"));
            
        // Check if instructor owns the course
        if (!course.getInstructor().getId().equals(instructor.getId())) {
            redirectAttributes.addFlashAttribute("error", "You don't have permission to grade students in this course");
            return "redirect:/instructor/dashboard";
        }
        
        Student student = course.getEnrolledStudents().stream()
            .filter(s -> s.getId().equals(studentId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Student not found in this course"));
            
        Grade grade = new Grade();
        grade.setStudent(student);
        grade.setCourse(course);
        grade.setScore(score);
        grade.setComments(comments);
        
        gradeService.createGrade(grade);
        
        redirectAttributes.addFlashAttribute("success", "Grade submitted successfully");
        return "redirect:/instructor/dashboard";
    }

    @PostMapping("/students/grade")
    @Transactional(noRollbackFor = {RuntimeException.class})
    public String gradeStudent(@RequestParam("studentId") String studentId,
                             @RequestParam("courseId") Long courseId,
                             @RequestParam("grade") String grade,
                             HttpSession session,
                             Model model) {
        // Validate session
        String instructorId = (String) session.getAttribute("instructorId");
        if (instructorId == null) {
            return "redirect:/instructor/login";
        }

        try {
            // Validate instructor
            Instructor instructor = instructorService.findByInstructorId(instructorId);
            if (instructor == null) {
                session.invalidate();
                return "redirect:/instructor/login";
            }

            // Validate course
            Optional<Course> courseOpt = courseService.getCourseById(courseId);
            if (courseOpt.isEmpty()) {
                model.addAttribute("error", "Course not found");
                return showDashboard(model, session);
            }
            Course course = courseOpt.get();

            // Verify instructor owns the course
            if (!course.getInstructor().equals(instructor)) {
                model.addAttribute("error", "You don't have permission to grade this course");
                return showDashboard(model, session);
            }

            // Validate student
            Student student = studentService.findByStudentId(studentId);
            if (student == null) {
                model.addAttribute("error", "Student not found");
                return showDashboard(model, session);
            }

            // Verify student enrollment
            if (!course.getEnrolledStudents().contains(student)) {
                model.addAttribute("error", "Student is not enrolled in this course");
                return showDashboard(model, session);
            }

            // Update grade
            boolean success = studentService.updateGrade(studentId, courseId, grade);
            if (success) {
                model.addAttribute("success", "Grade updated successfully");
            } else {
                model.addAttribute("error", "Failed to update grade. Please check the grade format (A, B, C, D, or F)");
            }
            
            return showDashboard(model, session);
        } catch (Exception e) {
            logger.error("Error in gradeStudent: " + e.getMessage(), e);
            model.addAttribute("error", "An unexpected error occurred while updating the grade");
            return showDashboard(model, session);
        }
    }

    @PostMapping("/courses/content/delete/{contentId}")
    @Transactional
    public String deleteCourseContent(@PathVariable Long contentId,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        String instructorId = (String) session.getAttribute("instructorId");
        if (instructorId == null) {
            return "redirect:/instructor/login";
        }
        
        Instructor instructor = instructorService.findByInstructorId(instructorId);
        if (instructor == null) {
            session.invalidate();
            return "redirect:/instructor/login";
        }
        
        try {
            CourseContent content = courseContentService.getCourseContentById(contentId);
            Course course = content.getCourse();
            
            // Check if instructor owns the course
            if (!course.getInstructor().getId().equals(instructor.getId())) {
                redirectAttributes.addFlashAttribute("error", "You don't have permission to delete this content");
                return "redirect:/instructor/dashboard";
            }
            
            courseContentService.deleteCourseContent(contentId);
            redirectAttributes.addFlashAttribute("success", "Course content deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting course content", e);
            redirectAttributes.addFlashAttribute("error", "Failed to delete course content");
        }
        
        return "redirect:/instructor/dashboard";
    }
} 