package com.example.courseregistrationsystem.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@Table(name = "courses")
@ToString(exclude = {"enrolledStudents", "instructor"})
@EqualsAndHashCode(exclude = {"enrolledStudents", "instructor"})
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String courseCode;
    private String courseName;
    private String description;
    private Integer credits;
    private Integer capacity;
    
    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "course_enrollments",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<Student> enrolledStudents = new HashSet<>();
    
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CourseContent> contentItems = new ArrayList<>();
    
    // This method is needed for AssignmentService
    public Long getId() {
        return this.id;
    }
} 