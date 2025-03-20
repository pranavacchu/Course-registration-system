package com.example.courseregistrationsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.courseregistrationsystem.model.Student;
import com.example.courseregistrationsystem.service.StudentService;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        return student != null ? ResponseEntity.ok(student) : ResponseEntity.notFound().build();
    }
    
    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
        Student student = studentService.updateStudent(id, studentDetails);
        return student != null ? ResponseEntity.ok(student) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{studentId}/enroll/{courseId}")
    public ResponseEntity<Student> enrollInCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        Student student = studentService.enrollInCourse(studentId, courseId);
        return student != null ? ResponseEntity.ok(student) : ResponseEntity.badRequest().build();
    }
    
    @PostMapping("/{studentId}/drop/{courseId}")
    public ResponseEntity<Student> dropCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        Student student = studentService.dropCourse(studentId, courseId);
        return student != null ? ResponseEntity.ok(student) : ResponseEntity.badRequest().build();
    }
} 