package com.vijay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vijay.entity.Column;

@Repository
public interface ColumnRepository extends JpaRepository<Column, Long> {

}
