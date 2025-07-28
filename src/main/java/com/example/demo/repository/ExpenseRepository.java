package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Expense;
import com.example.demo.entity.User;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	
	//ログイン中のユーザーの支出を取得
	List<Expense> findByUser(User user);
	
	//特定ユーザーの、ある月の支出を取得
	List<Expense> findByUserAndDateBetween(User user,LocalDate start,LocalDate end);
	
	//IDで支出レコードの1件のみを指定して取得、サービスクラスで呼び出したユーザーの支出かチェックする
	Optional<Expense> findById(Long id);
	
	//必要に応じて日付やカテゴリで絞るメソッドも追加可能
	
	//GoalSummaryで月の支出累計額を取得で使用
	@Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user = :user AND e.date BETWEEN :start AND :end")
	Integer findTotalExpenseByUserAndDateBetween(User user, LocalDate start, LocalDate end);
	
	//月ごとの感情、ポジティブ、ネガティブを集計して取得 
	@Query("""
			SELECT MONTH(e.date),
					CASE WHEN e.feelingIsDefault = true THEN df.polarity ELSE uf.polarity END,
					COUNT(e)
			FROM Expense e 
			LEFT JOIN e.defaultFeeling df
			LEFT JOIN e.userFeeling uf
			WHERE e.user = :user AND YEAR(e.date) = :year
			GROUP BY MONTH(e.date),
					CASE WHEN e.feelingIsDefault = true THEN df.polarity ELSE uf.polarity END
		""")
	List<Object[]> countFeelingsByPolarityPerMonth(User user, int year);
	
	//年間の感情、ポジティブ、ネガティブを集計して取得
	@Query("""
			SELECT 
				CASE 
					WHEN e.feelingIsDefault = true THEN df.polarity ELSE uf.polarity END,
				COUNT(e)
			FROM Expense e 
			LEFT JOIN e.defaultFeeling df 
			LEFT JOIN e.userFeeling uf 
			WHERE e.user = :user AND YEAR(e.date) = :year 
			GROUP BY 
				 CASE
					WHEN e.feelingIsDefault = true THEN df.polarity 
					ELSE uf.polarity 
					END
		""")
	List<Object[]> countTotalFeelingsByPolarity(User user, int year);
	
	//ホーム画面で当月のみポジティブ、ネガティブ集計結果を表示のため取得
	@Query("""
			SELECT 
				CASE WHEN e.feelingIsDefault = true THEN df.polarity ELSE uf.polarity END,
				COUNT(e)
			FROM Expense e 
			LEFT JOIN e.defaultFeeling df
			LEFT JOIN e.userFeeling uf 
			WHERE e.user = :user AND e.date BETWEEN :start AND :end 
			GROUP BY 
				CASE WHEN e.feelingIsDefault = true THEN df.polarity ELSE uf.polarity END
			""")
	List<Object[]> countFeelingsByPolarity(@Param("user") User user, @Param("start") LocalDate start, @Param("end") LocalDate end);
	

}
