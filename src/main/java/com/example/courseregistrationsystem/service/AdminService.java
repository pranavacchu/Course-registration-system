package com.example.courseregistrationsystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.courseregistrationsystem.model.Admin;
import com.example.courseregistrationsystem.repository.AdminRepository;

@Service
public class AdminService {
    
    @Autowired
    private AdminRepository adminRepository;
    
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }
    
    public Admin getAdminById(Long id) {
        Optional<Admin> admin = adminRepository.findById(id);
        return admin.orElse(null);
    }
    
    public Admin createAdmin(Admin admin) {
        return adminRepository.save(admin);
    }
    
    public Admin updateAdmin(Long id, Admin adminDetails) {
        Admin admin = getAdminById(id);
        if (admin != null) {
            admin.setAdminId(adminDetails.getAdminId());
            admin.setUsername(adminDetails.getUsername());
            admin.setEmail(adminDetails.getEmail());
            admin.setDepartment(adminDetails.getDepartment());
            return adminRepository.save(admin);
        }
        return null;
    }
    
    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }
    
    public Admin findByAdminId(String adminId) {
        return adminRepository.findByAdminId(adminId);
    }
} 