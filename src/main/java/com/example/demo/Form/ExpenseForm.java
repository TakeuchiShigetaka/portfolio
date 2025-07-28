package com.example.demo.Form;



import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class ExpenseForm {
	
	@NotNull(message="入力してください。")
	@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$",message="日付はyyyy-MM-dd形式で入力してください。")
	private String date;
	
	@NotNull(message="入力してください。")
	private Long categoryId;
	
	@NotNull(message="入力してください。")
	private Long feelingId;
	
	@NotNull(message="入力してください。")
	@Min(value=1, message="1円以上から10000000円以下入力してください。")
	@Max(value=10000000, message="1円以上から10000000円以下で入力してください")
	private Integer amount;
	
	//SQL定義で　memoカラムの未入力時は空白が入るためNotNullは付けないことにする
	@Size(max= 255,message= "255桁以下で入力してください。")
	private String memo;

}
