package com.example.courseregistrationsystem.service;

import java.util.List;

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
        return studentRepository.findByStudentIdWithCourses(studentId);
    }
    
    public Student createStudent(Student student) {
        // Check if student already exists
        if (studentRepository.findByStudentId(student.getStudentId()) != null) {
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
        Student student = findByStudentId(studentId);
        if (student == null) {
            throw new RuntimeException("Student not found");
        }
        
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            throw new RuntimeException("Course not found");
        }
        
        if (student.getEnrolledCourses().contains(course)) {
            throw new RuntimeException("Already enrolled in this course");
        }
        
        if (course.getEnrolledStudents().size() >= course.getCapacity()) {
            throw new RuntimeException("Course is full");
        }
        
        student.getEnrolledCourses().add(course);
        course.getEnrolledStudents().add(student);
        
        return studentRepository.save(student);
    }
    
    public Student dropCourse(String studentId, Long courseId) {
        Student student = findByStudentId(studentId);
        if (student == null) {
            throw new RuntimeException("Student not found");
        }
        
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            throw new RuntimeException("Course not found");
        }
        
        if (!student.getEnrolledCourses().contains(course)) {
            throw new RuntimeException("Not enrolled in this course");
        }
        
        student.getEnrolledCourses().remove(course);
        course.getEnrolledStudents().remove(student);
        
        return studentRepository.save(student);
    }
} 