package com.example.courseregistrationsystem.service;

import com.example.courseregistrationsystem.model.Grade;
import com.example.courseregistrationsystem.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GradeService {
    private final GradeRepository gradeRepository;

    @Autowired
    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    public Grade createGrade(Grade grade) {
        return gradeRepository.save(grade);
    }

    public List<Grade> getGradesByStudentId(Long studentId) {
        return gradeRepository.findByStudentId(studentId);
    }

    public List<Grade> getGradesByCourseId(Long courseId) {
        return gradeRepository.findByCourseId(courseId);
    }

    public List<Grade> getGradesByStudentIdAndCourseId(Long studentId, Long courseId) {
        return gradeRepository.findByStudentIdAndCourseIdOrderByGradedAtDesc(studentId, courseId);
    }

    public Grade getGradeById(Long id) {
        return gradeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Grade not found"));
    }

    public Grade updateGrade(Long id, Grade updatedGrade) {
        Grade existingGrade = getGradeById(id);
        existingGrade.setScore(updatedGrade.getScore());
        existingGrade.setLetterGrade(updatedGrade.getLetterGrade());
        existingGrade.setComments(updatedGrade.getComments());
        return gradeRepository.save(existingGrade);
    }

    public void deleteGrade(Long id) {
        if (!gradeRepository.existsById(id)) {
            throw new IllegalArgumentException("Grade not found");
        }
        gradeRepository.deleteById(id);
    }
} 