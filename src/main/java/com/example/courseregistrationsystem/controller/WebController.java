package com.example.courseregistrationsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    
    @GetMapping("/")
    public String home() {
        return "index";
    }
    
    @GetMapping("/courses")
    public String courses() {
        return "courses";
    }
    
    @GetMapping("/students")
    public String students() {
        return "students";
    }
    
    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }
} 