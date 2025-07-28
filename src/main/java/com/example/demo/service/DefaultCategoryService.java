package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entity.DefaultCategory;
import com.example.demo.repository.DefaultCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultCategoryService {
	
	private final DefaultCategoryRepository defaultCategoryRepository;
	
	//全件取得
	public List<DefaultCategory> findAll(){
		return defaultCategoryRepository.findAll();
	}
	
	//IDで1件取得（Optional型で返す）
	public Optional<DefaultCategory> findById(Long id){
		return defaultCategoryRepository.findById(id);
	}

}
