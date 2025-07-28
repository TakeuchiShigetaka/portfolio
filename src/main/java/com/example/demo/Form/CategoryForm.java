package com.example.demo.Form;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CategoryForm {
	
	@NotNull(message="入力してください。")
	private String Name;

}
