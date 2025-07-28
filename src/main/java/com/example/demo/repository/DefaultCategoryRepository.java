package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.DefaultCategory;

public interface DefaultCategoryRepository extends JpaRepository<DefaultCategory, Long> {

}
