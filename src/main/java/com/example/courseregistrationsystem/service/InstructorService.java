package com.example.courseregistrationsystem.service;

import java.util.List;

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
    
    public Instructor getInstructorById(Long id) {
        return instructorRepository.findById(id).orElse(null);
    }
    
    public Instructor findByInstructorId(String instructorId) {
        return instructorRepository.findByInstructorId(instructorId);
    }
    
    public Instructor findByInstructorIdWithCourses(String instructorId) {
        return instructorRepository.findByInstructorIdWithCourses(instructorId);
    }
    
    public Instructor createInstructor(Instructor instructor) {
        // Check if instructor already exists
        if (instructorRepository.findByInstructorId(instructor.getInstructorId()) != null) {
            throw new RuntimeException("Instructor ID already exists");
        }
        
        // Set default values if not set
        if (instructor.getRole() == null) {
            instructor.setRole(User.UserRole.INSTRUCTOR);
        }
        
        return instructorRepository.save(instructor);
    }
    
    public Instructor updateInstructor(Long id, Instructor instructorDetails) {
        Instructor instructor = getInstructorById(id);
        if (instructor != null) {
            instructor.setUsername(instructorDetails.getUsername());
            instructor.setEmail(instructorDetails.getEmail());
            instructor.setInstructorId(instructorDetails.getInstructorId());
            return instructorRepository.save(instructor);
        }
        return null;
    }
    
    public void deleteInstructor(Long id) {
        instructorRepository.deleteById(id);
    }
} 