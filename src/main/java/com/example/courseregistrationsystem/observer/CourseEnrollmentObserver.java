package com.example.courseregistrationsystem.observer;

import com.example.courseregistrationsystem.model.Course;
import com.example.courseregistrationsystem.model.Student;

public interface CourseEnrollmentObserver {
    void onStudentEnrolled(Student student, Course course);
    void onStudentDropped(Student student, Course course);
} 