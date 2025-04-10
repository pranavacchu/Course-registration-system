package com.example.courseregistrationsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.courseregistrationsystem.model.Course;
import com.example.courseregistrationsystem.model.Instructor;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByInstructor(Instructor instructor);
    Optional<Course> findByCourseCode(String courseCode);
    
    @Query("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.instructor LEFT JOIN FETCH c.enrolledStudents")
    List<Course> findAllWithInstructorsAndStudents();

    @Query("SELECT DISTINCT c FROM Course c " +
           "LEFT JOIN FETCH c.instructor " +
           "LEFT JOIN FETCH c.enrolledStudents " +
           "LEFT JOIN FETCH c.contentItems " +
           "WHERE c.instructor = :instructor")
    List<Course> findByInstructorWithEnrolledStudents(Instructor instructor);
} 