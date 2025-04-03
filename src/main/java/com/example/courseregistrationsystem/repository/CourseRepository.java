package com.example.courseregistrationsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.courseregistrationsystem.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByCourseCode(String courseCode);
    
    @Query("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.instructor LEFT JOIN FETCH c.enrolledStudents")
    List<Course> findAllWithInstructorsAndStudents();
} 