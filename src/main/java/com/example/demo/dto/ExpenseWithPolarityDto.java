package com.example.demo.dto;

import com.example.demo.entity.Expense;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExpenseWithPolarityDto {

	private final Expense expense;
	private String polarity;
	
	public String getCategoryName() {
		
		if(expense == null) {
			return "";
		}
		return expense.getResolvedCategoryName();	
	}
	
	public String getFeelingName() {
		
		if(expense == null) {
			return "";
		}
		return expense.getResolvedFeelingName();
	}

}
