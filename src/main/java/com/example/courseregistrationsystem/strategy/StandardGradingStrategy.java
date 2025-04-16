package com.example.courseregistrationsystem.strategy;

public class StandardGradingStrategy implements GradingStrategy {
    @Override
    public String calculateGrade(double score) {
        if (score >= 90) return "A";
        if (score >= 80) return "B";
        if (score >= 70) return "C";
        if (score >= 60) return "D";
        return "F";
    }

    @Override
    public boolean isValidGrade(String grade) {
        if (grade == null || grade.trim().isEmpty()) {
            return false;
        }
        String normalizedGrade = grade.trim().toUpperCase();
        return normalizedGrade.matches("[A-F][+-]?");
    }
} 