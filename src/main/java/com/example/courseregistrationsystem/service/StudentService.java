package com.example.courseregistrationsystem.service;

import java.util.List;
import java.util.HashSet;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.courseregistrationsystem.model.Course;
import com.example.courseregistrationsystem.model.Student;
import com.example.courseregistrationsystem.model.User;
import com.example.courseregistrationsystem.repository.CourseRepository;
import com.example.courseregistrationsystem.repository.StudentRepository;

@Service
@Transactional
public class StudentService {
    
    private static final Logger logger = Logger.getLogger(StudentService.class.getName());
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }
    
    public Student findByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));
    }
    
    public Student findByStudentIdWithCourses(String studentId) {
        return studentRepository.findByStudentIdWithCourses(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));
    }
    
    public Student createStudent(Student student) {
        // Check if student already exists
        if (studentRepository.findByStudentId(student.getStudentId()).isPresent()) {
            throw new RuntimeException("Student ID already exists");
        }
        
        // Set default values if not set
        if (student.getRole() == null) {
            student.setRole(User.UserRole.STUDENT);
        }
        
        return studentRepository.save(student);
    }
    
    public Student updateStudent(Long id, Student studentDetails) {
        Student student = getStudentById(id);
        if (student != null) {
            student.setUsername(studentDetails.getUsername());
            student.setEmail(studentDetails.getEmail());
            student.setStudentId(studentDetails.getStudentId());
            return studentRepository.save(student);
        }
        return null;
    }
    
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
    
    public Student enrollInCourse(String studentId, Long courseId) {
        Student student = findByStudentIdWithCourses(studentId);
        if (student == null) {
            throw new RuntimeException("Student not found");
        }
        
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            throw new RuntimeException("Course not found");
        }
        
        // Check if student is already enrolled in this course
        if (student.getEnrolledCourses().contains(course)) {
            throw new RuntimeException("Already enrolled in this course");
        }
        
        // Check if course is full
        if (course.getEnrolledStudents() != null && course.getEnrolledStudents().size() >= course.getCapacity()) {
            throw new RuntimeException("Course is full");
        }
        
        // Add course to student's enrolled courses
        student.getEnrolledCourses().add(course);
        
        // Add student to course's enrolled students
        if (course.getEnrolledStudents() == null) {
            course.setEnrolledStudents(new HashSet<>());
        }
        course.getEnrolledStudents().add(student);
        
        // Save both entities to ensure the relationship is properly persisted
        courseRepository.save(course);
        return studentRepository.save(student);
    }
    
    public Student dropCourse(String studentId, Long courseId) {
        Student student = findByStudentIdWithCourses(studentId);
        if (student == null) {
            throw new RuntimeException("Student not found");
        }
        
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            throw new RuntimeException("Course not found");
        }
        
        // Remove course from student's enrolled courses
        student.getEnrolledCourses().remove(course);
        
        // Remove student from course's enrolled students
        if (course.getEnrolledStudents() != null) {
            course.getEnrolledStudents().remove(student);
        }
        
        // Save both entities to ensure the relationship is properly persisted
        courseRepository.save(course);
        return studentRepository.save(student);
    }

    @Transactional
    public void updateGrade(String studentId, Long courseId, String grade) {
        try {
            Student student = findByStudentId(studentId);
            if (student == null) {
                throw new RuntimeException("Student not found");
            }

            // Validate grade format
            if (!isValidGrade(grade)) {
                throw new RuntimeException("Invalid grade format. Please use A, B, C, D, or F");
            }

            // Update the grade in the student's grades map
            Map<Long, String> grades = student.getGrades();
            if (grades == null) {
                grades = new HashMap<>();
                student.setGrades(grades);
            }
            grades.put(courseId, grade);
            
            studentRepository.save(student);
        } catch (Exception e) {
            logger.severe("Error updating grade for student " + studentId + " in course " + courseId + ": " + e.getMessage());
            throw new RuntimeException("Failed to update grade: " + e.getMessage());
        }
    }

    private boolean isValidGrade(String grade) {
        if (grade == null || grade.trim().isEmpty()) {
            return false;
        }
        String normalizedGrade = grade.trim().toUpperCase();
        return normalizedGrade.matches("[A-F][+-]?");
    }
} 