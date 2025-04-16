package com.example.courseregistrationsystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.courseregistrationsystem.model.Instructor;
import com.example.courseregistrationsystem.model.User;
import com.example.courseregistrationsystem.repository.InstructorRepository;

@Service
@Transactional
public class InstructorService {
    
    @Autowired
    private InstructorRepository instructorRepository;
    
    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }
    
    public Optional<Instructor> getInstructorById(Long id) {
        return instructorRepository.findById(id);
    }
    
    public Instructor findByInstructorId(String instructorId) {
        return instructorRepository.findByInstructorId(instructorId)
            .orElseThrow(() -> new RuntimeException("Instructor not found with ID: " + instructorId));
    }
    
    public Instructor findByInstructorIdWithCourses(String instructorId) {
        return instructorRepository.findByInstructorIdWithCourses(instructorId)
            .orElseThrow(() -> new RuntimeException("Instructor not found with ID: " + instructorId));
    }
    
    public Instructor findByEmail(String email) {
        return instructorRepository.findByEmail(email);
    }
    
    public Instructor createInstructor(Instructor instructor) {
        // Check if instructor already exists
        if (instructorRepository.findByInstructorId(instructor.getInstructorId()).isPresent()) {
            throw new RuntimeException("Instructor ID already exists");
        }
        
        // Set default values if not set
        if (instructor.getRole() == null) {
            instructor.setRole(User.UserRole.INSTRUCTOR);
        }
        
        return instructorRepository.save(instructor);
    }
    
    public Instructor updateInstructor(Instructor instructor) {
        if (!instructorRepository.existsById(instructor.getId())) {
            throw new IllegalArgumentException("Instructor not found");
        }
        return instructorRepository.save(instructor);
    }
    
    public void deleteInstructor(Long id) {
        instructorRepository.deleteById(id);
    }
} 