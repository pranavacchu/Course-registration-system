package com.example.courseregistrationsystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.courseregistrationsystem.model.Course;
import com.example.courseregistrationsystem.model.Instructor;
import com.example.courseregistrationsystem.repository.CourseRepository;

@Service
@Transactional
public class CourseService {
    
    private final CourseRepository courseRepository;
    
    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }
    
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    
    public List<Course> getAvailableCourses() {
        return courseRepository.findAllWithInstructorsAndStudents();
    }
    
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }
    
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }
    
    public Course updateCourse(Course course) {
        if (!courseRepository.existsById(course.getId())) {
            throw new IllegalArgumentException("Course not found");
        }
        return courseRepository.save(course);
    }
    
    @Transactional
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + id));
        
        // Clear the enrolled students relationship
        course.getEnrolledStudents().clear();
        
        // Clear the instructor relationship
        course.setInstructor(null);
        
        // Save the changes to clear relationships
        courseRepository.save(course);
        
        // Now delete the course (this will cascade to course content due to CascadeType.ALL)
        courseRepository.delete(course);
    }
    
    public Optional<Course> findByCourseCode(String courseCode) {
        return courseRepository.findByCourseCode(courseCode);
    }
    
    public List<Course> getCoursesByInstructor(Instructor instructor) {
        return courseRepository.findByInstructorWithEnrolledStudents(instructor);
    }

    @Transactional(readOnly = true)
    public List<Course> findByInstructorWithEnrolledStudents(Instructor instructor) {
        return courseRepository.findByInstructorWithEnrolledStudents(instructor);
    }

    @Transactional(readOnly = true)
    public List<Course> getAllCoursesWithRelationships() {
        return courseRepository.findAllWithInstructorsAndStudents();
    }
} 