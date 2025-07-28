package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;
import com.example.demo.entity.UserFeeling;

@Repository
public interface FeelingRepository extends JpaRepository<UserFeeling, Long> {
	
	List<UserFeeling> findByUser(User user);
	
	List<UserFeeling> findById(User user);
	
	//ユーザーと感情名で検索
	Optional<UserFeeling> findByUserAndName(User user, String name);
	
	//ユーザーとIDで検索
	Optional<UserFeeling> findByUserAndId(User user, Long id);
}
