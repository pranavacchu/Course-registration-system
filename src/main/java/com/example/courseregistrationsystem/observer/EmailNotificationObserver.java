package com.example.courseregistrationsystem.observer;

import com.example.courseregistrationsystem.model.Course;
import com.example.courseregistrationsystem.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailNotificationObserver implements CourseEnrollmentObserver {
    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationObserver.class);

    @Override
    public void onStudentEnrolled(Student student, Course course) {
        // In a real application, this would send an actual email
        logger.info("Sending enrollment confirmation email to {} for course {}", 
            student.getEmail(), course.getCourseCode());
    }

    @Override
    public void onStudentDropped(Student student, Course course) {
        // In a real application, this would send an actual email
        logger.info("Sending course drop confirmation email to {} for course {}", 
            student.getEmail(), course.getCourseCode());
    }
} 