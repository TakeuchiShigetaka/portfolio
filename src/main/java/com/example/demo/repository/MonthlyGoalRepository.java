package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.MonthlyGoal;
import com.example.demo.entity.User;

public interface MonthlyGoalRepository extends JpaRepository<MonthlyGoal, Long> {
	
	Optional<MonthlyGoal> findByUserIdAndYearAndMonth (Long userId, int year, int month);
	
	Optional<MonthlyGoal> findByUserAndYearAndMonth(User user, int year, int month);
	
	@Query("SELECT m.goalAmount FROM MonthlyGoal m WHERE m.user = :user AND m.year = :year AND m.month = :month")
	Integer findGoalAmountByUserAndYearAndMonth(User user, int year, int month);

}
