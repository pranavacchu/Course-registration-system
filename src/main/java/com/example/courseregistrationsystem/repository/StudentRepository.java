package com.example.courseregistrationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import com.example.courseregistrationsystem.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentId(String studentId);
    Optional<Student> findByUsername(String username);
    Optional<Student> findByEmail(String email);
    
    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.enrolledCourses WHERE s.studentId = :studentId")
    Optional<Student> findByStudentIdWithCourses(@Param("studentId") String studentId);
} 