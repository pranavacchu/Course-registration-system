package com.example.courseregistrationsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("ADMIN")
public class Admin extends User {
    @Column(unique = true)
    private String adminId;
    private String department;
} 