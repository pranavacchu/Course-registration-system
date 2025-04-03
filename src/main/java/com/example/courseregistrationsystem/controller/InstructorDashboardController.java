package com.example.courseregistrationsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.courseregistrationsystem.model.Instructor;
import com.example.courseregistrationsystem.service.InstructorService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/instructor")
public class InstructorDashboardController {

    @Autowired
    private InstructorService instructorService;

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String instructorId = (String) session.getAttribute("instructorId");
        
        if (instructorId == null) {
            redirectAttributes.addFlashAttribute("error", "Please login to access the dashboard");
            return "redirect:/instructor/login";
        }

        Instructor instructor = instructorService.findByInstructorIdWithCourses(instructorId);
        if (instructor == null) {
            redirectAttributes.addFlashAttribute("error", "Instructor not found");
            return "redirect:/instructor/login";
        }

        model.addAttribute("instructor", instructor);
        return "instructor-dashboard";
    }
} 