package com.example.courseregistrationsystem.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@Table(name = "students")
@ToString(exclude = "enrolledCourses")
@EqualsAndHashCode(callSuper = true, exclude = "enrolledCourses")
@DiscriminatorValue("STUDENT")
public class Student extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String studentId;
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "student_course_enrollments",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> enrolledCourses = new HashSet<>();
    
    @Transient
    private Map<Long, String> grades = new HashMap<>();
    
    @PrePersist
    public void prePersist() {
        if (getRole() == null) {
            setRole(UserRole.STUDENT);
        }
    }
    
    // Helper method to add a course
    public void enrollInCourse(Course course) {
        if (enrolledCourses == null) {
            enrolledCourses = new HashSet<>();
        }
        enrolledCourses.add(course);
    }
    
    // Helper method to remove a course
    public void dropCourse(Course course) {
        if (enrolledCourses != null) {
            enrolledCourses.remove(course);
        }
    }
    
    // Helper method to check if enrolled in a course
    public boolean isEnrolledIn(Course course) {
        return enrolledCourses != null && enrolledCourses.contains(course);
    }
} 