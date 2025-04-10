package com.example.courseregistrationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.courseregistrationsystem.model.Instructor;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    Optional<Instructor> findByInstructorId(String instructorId);
    Instructor findByUsername(String username);
    Instructor findByEmail(String email);
    
    @Query("SELECT i FROM Instructor i LEFT JOIN FETCH i.courses WHERE i.instructorId = :instructorId")
    Optional<Instructor> findByInstructorIdWithCourses(String instructorId);
} 