package com.vijay.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vijay.entity.Column;

@Repository
public interface ColumnRepository extends JpaRepository<Column, Long> {
    Optional<Column> findByTitleIgnoreCase(String title);
}

