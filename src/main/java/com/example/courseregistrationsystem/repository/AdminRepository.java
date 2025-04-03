package com.example.courseregistrationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.courseregistrationsystem.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByAdminId(String adminId);
} 