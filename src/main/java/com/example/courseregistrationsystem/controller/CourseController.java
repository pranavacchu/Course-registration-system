package com.example.courseregistrationsystem.controller;

import com.example.courseregistrationsystem.model.Course;
import com.example.courseregistrationsystem.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    
    @Autowired
    private CourseService courseService;
    
    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public Course createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course courseDetails) {
        return courseService.getCourseById(id)
            .map(existingCourse -> {
                existingCourse.setCourseCode(courseDetails.getCourseCode());
                existingCourse.setCourseName(courseDetails.getCourseName());
                existingCourse.setDescription(courseDetails.getDescription());
                existingCourse.setCredits(courseDetails.getCredits());
                existingCourse.setCapacity(courseDetails.getCapacity());
                existingCourse.setInstructor(courseDetails.getInstructor());
                return ResponseEntity.ok(courseService.updateCourse(existingCourse));
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        if (courseService.getCourseById(id).isPresent()) {
            courseService.deleteCourse(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
} 