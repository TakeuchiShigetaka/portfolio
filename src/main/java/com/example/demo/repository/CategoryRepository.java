package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;
import com.example.demo.entity.UserCategory;

@Repository
public interface CategoryRepository extends JpaRepository<UserCategory, Long> {

	List<UserCategory> findByUser(User user);
	
	List<UserCategory> findById(User user);
	
	//ユーザーとカテゴリ名で検索
	Optional<UserCategory> findByUserAndName(User user, String name);
	
	//ユーザーとIDで検索
	Optional<UserCategory> findByUserAndId(User user, Long id);
}
