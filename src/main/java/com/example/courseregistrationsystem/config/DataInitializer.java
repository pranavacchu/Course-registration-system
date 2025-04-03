package com.example.courseregistrationsystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.courseregistrationsystem.model.Admin;
import com.example.courseregistrationsystem.model.User;
import com.example.courseregistrationsystem.repository.AdminRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if default admin exists
        if (adminRepository.findByAdminId("admin") == null) {
            // Create default admin account
            Admin admin = new Admin();
            admin.setAdminId("admin");
            admin.setPassword("admin123");
            admin.setUsername("Administrator");
            admin.setEmail("admin@coursesystem.com");
            admin.setRole(User.UserRole.ADMIN);
            
            adminRepository.save(admin);
        }
    }
} 