package com.example.courseregistrationsystem.service;

import com.example.courseregistrationsystem.model.Student;
import com.example.courseregistrationsystem.model.Course;
import com.example.courseregistrationsystem.repository.StudentRepository;
import com.example.courseregistrationsystem.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    public Student getStudentById(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        return student.orElse(null);
    }
    
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }
    
    public Student updateStudent(Long id, Student studentDetails) {
        Student student = getStudentById(id);
        if (student != null) {
            student.setStudentId(studentDetails.getStudentId());
            student.setName(studentDetails.getName());
            student.setEmail(studentDetails.getEmail());
            return studentRepository.save(student);
        }
        return null;
    }
    
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
    
    public Student findByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId);
    }
    
    public Student enrollInCourse(Long studentId, Long courseId) {
        Student student = getStudentById(studentId);
        Course course = courseRepository.findById(courseId).orElse(null);
        
        if (student != null && course != null) {
            if (course.getEnrolledStudents() < course.getCapacity()) {
                student.getEnrolledCourses().add(course);
                course.setEnrolledStudents(course.getEnrolledStudents() + 1);
                courseRepository.save(course);
                return studentRepository.save(student);
            }
        }
        return null;
    }
    
    public Student dropCourse(Long studentId, Long courseId) {
        Student student = getStudentById(studentId);
        Course course = courseRepository.findById(courseId).orElse(null);
        
        if (student != null && course != null) {
            student.getEnrolledCourses().remove(course);
            course.setEnrolledStudents(course.getEnrolledStudents() - 1);
            courseRepository.save(course);
            return studentRepository.save(student);
        }
        return null;
    }
} 