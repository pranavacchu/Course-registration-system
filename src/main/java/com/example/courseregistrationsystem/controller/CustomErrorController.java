package com.example.courseregistrationsystem.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object error = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        model.addAttribute("status", status != null ? status : "Unknown");
        model.addAttribute("error", error != null ? error : "Unknown error");
        model.addAttribute("message", message != null ? message : "An unexpected error occurred");

        return "error";
    }
} 