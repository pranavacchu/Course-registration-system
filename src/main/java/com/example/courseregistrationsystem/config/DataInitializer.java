package com.example.courseregistrationsystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.courseregistrationsystem.model.Admin;
import com.example.courseregistrationsystem.model.User;
import com.example.courseregistrationsystem.repository.AdminRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Starting data initialization...");
        
        // Check if default admin exists
        if (adminRepository.findByAdminId("admin").isEmpty()) {
            logger.info("Creating default admin account...");
            // Create default admin account
            Admin admin = new Admin();
            admin.setAdminId("admin");
            admin.setPassword("admin123");
            admin.setUsername("Administrator");
            admin.setEmail("admin@coursesystem.com");
            admin.setRole(User.UserRole.ADMIN);
            admin.setDepartment("System Administration");
            
            adminRepository.save(admin);
            logger.info("Default admin account created successfully");
        } else {
            logger.info("Default admin account already exists");
        }
        
        logger.info("Data initialization completed");
    }
} 