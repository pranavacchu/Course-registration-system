package com.example.courseregistrationsystem.repository;

import com.example.courseregistrationsystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByStudentId(String studentId);
    Student findByEmail(String email);
} 