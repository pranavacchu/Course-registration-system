package com.example.courseregistrationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {
    Optional<T> findById(ID id);
    List<T> findAll();
    <S extends T> S save(S entity);
    void deleteById(ID id);
    boolean existsById(ID id);
} 