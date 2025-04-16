package com.example.courseregistrationsystem.factory;

import com.example.courseregistrationsystem.model.Admin;
import com.example.courseregistrationsystem.model.Instructor;
import com.example.courseregistrationsystem.model.Student;
import com.example.courseregistrationsystem.model.User;
import com.example.courseregistrationsystem.model.User.UserRole;

public class UserFactory {
    public static User createUser(UserRole role, String username, String email, String password) {
        switch (role) {
            case ADMIN:
                Admin admin = new Admin();
                admin.setUsername(username);
                admin.setEmail(email);
                admin.setPassword(password);
                admin.setRole(UserRole.ADMIN);
                return admin;
                
            case INSTRUCTOR:
                Instructor instructor = new Instructor();
                instructor.setUsername(username);
                instructor.setEmail(email);
                instructor.setPassword(password);
                instructor.setRole(UserRole.INSTRUCTOR);
                return instructor;
                
            case STUDENT:
                Student student = new Student();
                student.setUsername(username);
                student.setEmail(email);
                student.setPassword(password);
                student.setRole(UserRole.STUDENT);
                return student;
                
            default:
                throw new IllegalArgumentException("Invalid user role: " + role);
        }
    }
} 