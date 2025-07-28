package com.example.demo.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.dto.GoalSummaryDto;
import com.example.demo.dto.MonthlyPolarityCountDto;
import com.example.demo.entity.User;
import com.example.demo.repository.ExpenseRepository;
import com.example.demo.repository.MonthlyGoalRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoalSummaryService {
	
	private final MonthlyGoalRepository monthlyGoalRepository;
	private final ExpenseRepository expenseRepository;
	
	public List<GoalSummaryDto> getGoalSummaryForYear(User user, int year){
		List<GoalSummaryDto> result = new ArrayList<>();
		
		for(int month = 1; month <= 12; month++) {
			//年月から月初と月末を生成
			YearMonth ym = YearMonth.of(year,month);
			LocalDate start = ym.atDay(1);
			LocalDate end = ym.atEndOfMonth();
			
			Integer goalAmount = monthlyGoalRepository.findGoalAmountByUserAndYearAndMonth(user, year,month);
			Integer totalExpense = expenseRepository.findTotalExpenseByUserAndDateBetween(user, start, end);
			
			result.add(new GoalSummaryDto(month, goalAmount, totalExpense));
		}
		
		return result;
	}
	
	public Map<Integer, MonthlyPolarityCountDto> getMonthlyPolarityCountMap(User user, int year) {
		List<Object[]> raw = expenseRepository.countFeelingsByPolarityPerMonth(user, year);
		Map<Integer, MonthlyPolarityCountDto> result = new HashMap<>();

		for (Object[] row : raw) {
			Integer month = (Integer) row[0];
			String polarity = (String) row[1];
			Long count = (Long) row[2];

			MonthlyPolarityCountDto dto = result.getOrDefault(month, new MonthlyPolarityCountDto(month, 0L, 0L));
			if ("positive".equals(polarity)) {
				dto.setPositiveCount(count);
			} else if ("negative".equals(polarity)) {
				dto.setNegativeCount(count);
			}
			result.put(month, dto);
		}
		return result;
	}
	
	public Map<String, Long> countTotalFeelingsByPolarity(User user, int year){
	    List<Object[]> raw = expenseRepository.countTotalFeelingsByPolarity(user, year);
	    Map<String, Long> result = new HashMap<>();

	    for (Object[] row : raw) {
	        if (row.length >= 2 && row[0] != null && row[1] != null) {
	            String polarity = (String) row[0];
	            Long count = (Long) row[1];
	            result.put(polarity, count);
	        } 
	    }

	    return result;
	}

}

