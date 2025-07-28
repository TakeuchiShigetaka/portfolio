package com.example.demo.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.HomeSummaryDto;
import com.example.demo.entity.User;
import com.example.demo.repository.ExpenseRepository;
import com.example.demo.repository.MonthlyGoalRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeSummaryService {
	
	private final ExpenseRepository expenseRepository;
	private final MonthlyGoalRepository monthlyGoalRepository;
	
	public HomeSummaryDto getSummary(User user, int year, int month) {
		
		YearMonth ym = YearMonth.of(year,month);
		LocalDate start = ym.atDay(1);
		LocalDate end = ym.atEndOfMonth();
		
		Integer goalAmount = monthlyGoalRepository.findGoalAmountByUserAndYearAndMonth(user,year, month);
		Integer totalExpense = expenseRepository.findTotalExpenseByUserAndDateBetween(user, start, end);
		
		List<Object[]> raw = expenseRepository.countFeelingsByPolarity(user, start, end);
		Long positive = 0L, negative = 0L;
		for(Object[] row : raw) {
			if(row[0] == null || row[1] == null) continue; //念のため防衛的パスワード
			String polarity = (String) row[0];
			Long count = (Long) row[1];
			if("positive".equals(polarity))
				positive = count;
			else if("negative".equals(polarity))
				negative = count;
		}
		
		return new HomeSummaryDto(year, month, goalAmount, totalExpense, positive, negative);
	}

}
