package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoalSummaryDto {
	
	private int month; //月
	private Integer totalGoalAmount; //目標額（nullalbe）
	private Integer totalExpenseAmount; //実支出額（nullalbe）

}
