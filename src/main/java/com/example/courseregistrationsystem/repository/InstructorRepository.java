package com.example.courseregistrationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.courseregistrationsystem.model.Instructor;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    Instructor findByInstructorId(String instructorId);
    Instructor findByUsername(String username);
    Instructor findByEmail(String email);
    
    @Query("SELECT i FROM Instructor i LEFT JOIN FETCH i.courses WHERE i.instructorId = :instructorId")
    Instructor findByInstructorIdWithCourses(@Param("instructorId") String instructorId);
} 