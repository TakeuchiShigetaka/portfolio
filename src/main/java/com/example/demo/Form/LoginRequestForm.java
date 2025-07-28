package com.example.demo.Form;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class LoginRequestForm {
	
	@NotNull(message="入力してください。")
	@Size(min=1, max=40, message="1文字から40文字で指定してください。")
	private String email;
	
	@NotNull(message="入力してください。")
	@Size(min=1, max=30, message="1文字から30文字で指定してください。")
	private String password;


}
