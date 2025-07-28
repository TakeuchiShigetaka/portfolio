package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.MonthlyCategoryGoals;
import com.example.demo.entity.User;

public interface MonthlyCategoryGoalsRepository extends JpaRepository<MonthlyCategoryGoals, Long> {
	
	List<MonthlyCategoryGoals> findByUserIdAndYearAndMonth(Long userId, int year, int month);
	
	List<MonthlyCategoryGoals> findByUserAndYearAndMonth(User user, int year, int month);
	
	@Query("""
			SELECT g FROM MonthlyCategoryGoals g
			WHERE g.year = :year And g.month =:month
			  AND (g.user = :user OR g.user IS NULL)
			""")
	List<MonthlyCategoryGoals> findByUserOrDefault(@Param("user") User user, @Param("year") int year, @Param("month") int month);
	
	Optional<MonthlyCategoryGoals> findByUserIdAndYearAndMonthAndCategoryIdAndCategoryIsDefault(
			Long userId, int year, int month, Long categoryId, boolean categoryIsDefault);
	
	//デフォルトカテゴリ保存用(user = null)
	Optional<MonthlyCategoryGoals> findByUserIsNullAndYearAndMonthAndCategoryId(
			int year, int month, Long categoryId );
	
	//ユーザーカテゴリ保存用(user = ユーザー)
	Optional<MonthlyCategoryGoals> findByUserAndYearAndMonthAndCategoryId(
			User user, int year, int month, Long categoryId);
	//月・年・ユーザーごとの削除や上書き保存用
	void deleteByUserAndYearAndMonth(User user, int year, int month);

}
