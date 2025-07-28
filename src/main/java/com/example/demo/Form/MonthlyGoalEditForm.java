package com.example.demo.Form;

import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class MonthlyGoalEditForm {
	
	private Long monthlyGoalId; //DB上のmonthly_goals.idが入る（あれば）
	
	private int year;
	private int month;
	
	@Min(value= 1, message= "1円以上から10000000円以下入力してください。")
	@Max(value= 10000000, message= "1円以上から10000000円以下入力してください。")
	@NotNull(message= "入力してください。")
	private Integer totalGoalAmount; //月全体の目標額
	
	private List<CategoryGoalDto> categoryGoals;
	
	@Data
	public static class CategoryGoalDto {
		private Long id; //DB上のmonthly_goals.idが入る（あれば）
		
		private Long categoryId;
		private String categoryName;
		private boolean isDefault;
		
		@Min(value= 1, message= "1円以上から10000000円以下入力してください。")
		@Max(value= 10000000, message= "1円以上から10000000円以下入力してください。")
		@NotNull(message= "入力してください。")
		private Integer goalAmount;
	}

}
