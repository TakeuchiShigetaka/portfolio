package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Expense;
import com.example.demo.entity.User;
import com.example.demo.repository.ExpenseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseService {

	private final ExpenseRepository expenseRepository;

	//支出の保存
	public void save(Expense expense) {
		expenseRepository.save(expense);
	}

	//ユーザーとカテゴリIDでの支出存在チェック
	public boolean existsByUserAndCategoryId(User user, Long categoryId) {
		return expenseRepository.findByUser(user).stream()
				.anyMatch(expense -> expense.getCategoryId().equals(categoryId) && !expense.isCategoryIsDefault());
	}

	//ユーザーと感情IDでの支出存在チェック
	public boolean existsByUserAndFeelingId(User user, Long feelingId) {
		return expenseRepository.findByUser(user).stream()
				.anyMatch(expense -> expense.getFeelingId().equals(feelingId) && !expense.isFeelingIsDefault());
	}

	//ユーザーの当月支出一覧を取得
	public List<Expense> findCurrentMonthExpenses(User user) {
		LocalDate start = LocalDate.now().withDayOfMonth(1);
		LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
		return expenseRepository.findByUserAndDateBetween(user, start, end);
	}

	//コントローラーより指定の月の支出をリストで取得
	public List<Expense> findByUserAndDateBetween(User user, LocalDate start, LocalDate end) {
		return expenseRepository.findByUserAndDateBetween(user, start, end);
	}

	//IDで支出レコードの1件のみを取得する、呼び出したユーザーの支出かチェックする
	public Expense findByIdAndUser(Long id, User user) {
		return expenseRepository.findById(id)
				.filter(expense -> expense.getUser().equals(user))
				.orElse(null);
	}

	//削除実行
	public void delete(Expense expense) {
		expenseRepository.delete(expense);
	}

	// 必要なら追加予定：
	// public List<Expense> findByUser(User user) { ... }
	// public List<Expense> findByMonth(...) { ... }

}