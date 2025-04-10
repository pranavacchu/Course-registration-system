package com.example.courseregistrationsystem.service;

import com.example.courseregistrationsystem.model.Course;
import com.example.courseregistrationsystem.model.CourseContent;
import com.example.courseregistrationsystem.repository.CourseContentRepository;
import com.example.courseregistrationsystem.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseContentService {
    private final CourseContentRepository courseContentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public CourseContentService(CourseContentRepository courseContentRepository, CourseRepository courseRepository) {
        this.courseContentRepository = courseContentRepository;
        this.courseRepository = courseRepository;
    }

    public CourseContent createCourseContent(CourseContent courseContent) {
        if (courseContent.getCourse() == null || courseContent.getCourse().getId() == null) {
            throw new IllegalArgumentException("Course must be specified");
        }
        
        // Verify course exists
        courseRepository.findById(courseContent.getCourse().getId())
            .orElseThrow(() -> new IllegalArgumentException("Course not found"));
            
        return courseContentRepository.save(courseContent);
    }

    public List<CourseContent> getCourseContentByCourseId(Long courseId) {
        return courseContentRepository.findByCourseId(courseId);
    }

    public CourseContent getCourseContentById(Long id) {
        return courseContentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Course content not found"));
    }

    public CourseContent updateCourseContent(Long id, CourseContent updatedContent) {
        CourseContent existingContent = getCourseContentById(id);
        
        existingContent.setTitle(updatedContent.getTitle());
        existingContent.setDescription(updatedContent.getDescription());
        existingContent.setContentType(updatedContent.getContentType());
        existingContent.setFileUrl(updatedContent.getFileUrl());
        
        return courseContentRepository.save(existingContent);
    }

    public void deleteCourseContent(Long id) {
        if (!courseContentRepository.existsById(id)) {
            throw new IllegalArgumentException("Course content not found");
        }
        courseContentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean isInstructorOfCourse(Long instructorId, Long courseId) {
        Optional<Course> course = courseRepository.findById(courseId);
        return course.isPresent() && course.get().getInstructor() != null && 
               course.get().getInstructor().getId().equals(instructorId);
    }
} 