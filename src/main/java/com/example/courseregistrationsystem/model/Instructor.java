package com.example.courseregistrationsystem.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("INSTRUCTOR")
public class Instructor extends User {
    @Column(unique = true)
    private String instructorId;
    
    private String department;
    
    @OneToMany(mappedBy = "instructor", fetch = jakarta.persistence.FetchType.LAZY)
    private Set<Course> courses = new HashSet<>();

    @PrePersist
    public void prePersist() {
        if (getRole() == null) {
            setRole(UserRole.INSTRUCTOR);
        }
    }
} 