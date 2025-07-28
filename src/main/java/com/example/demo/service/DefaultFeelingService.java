package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entity.DefaultFeeling;
import com.example.demo.repository.DefaultFeelingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultFeelingService {
	
	private final DefaultFeelingRepository defaultFeelingRepository;
	
	//全件取得
	public List<DefaultFeeling> findAll(){
		return defaultFeelingRepository.findAll();
	}
	
	//IDで1件取得（Optional型で返す）
	public Optional<DefaultFeeling> findById(Long id){
		return defaultFeelingRepository.findById(id);
	}

}
