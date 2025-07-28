package com.example.demo.Form;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class RegisterUserForm {
	
	@NotNull(message="入力してください。")
	@Size(min=1, max=30, message="1文字から30文字で指定してください。")
	private String name;
	
	@NotNull(message="入力してください。")
	@Size(min=8, max=40, message="8文字から40文字で指定してください。")
	private String email;
	
	@NotNull(message="入力してください。")
	@Size(min=4, max=30, message="4文字から30文字で指定してください。")
	private String password;
	
	@NotNull(message="入力してください。")
	@Size(min=4,max=30, message="4文字から30文字で指定してください。")
	private String confirmPassword;

}
