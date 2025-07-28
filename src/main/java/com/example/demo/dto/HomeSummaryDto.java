package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeSummaryDto {
	
	private int year;
	private int month;
	private Integer goalAmount;
	private Integer totalExpense;
	private Long positiveCount;
	private Long negativeCount;

}
