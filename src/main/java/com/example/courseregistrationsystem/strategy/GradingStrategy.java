package com.example.courseregistrationsystem.strategy;

public interface GradingStrategy {
    String calculateGrade(double score);
    boolean isValidGrade(String grade);
} 