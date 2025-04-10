package com.example.courseregistrationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.example.courseregistrationsystem.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByAdminId(String adminId);
} 