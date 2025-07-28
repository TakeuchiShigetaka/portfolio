package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyPolarityCountDto {
	
	private int month;
	private Long positiveCount;
	private Long negativeCount;

}
