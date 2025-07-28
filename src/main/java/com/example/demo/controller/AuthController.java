package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.Form.LoginRequestForm;
import com.example.demo.Form.RegisterUserForm;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {
	//※Gitコミットのため追加

	private final UserService userService;

	//ログイン画面の表示
	@GetMapping("/login")
	public String showLoginForm(@ModelAttribute LoginRequestForm form) {
		return "login";
	}

	//新規登録画面の表示
	@GetMapping("/register")
	public String showRegisterForm(@ModelAttribute RegisterUserForm form) {
		return "register";
	}

	//新規登録処理
	@PostMapping("/register")
	public String processRegister(@Validated @ModelAttribute RegisterUserForm form,
			BindingResult result) {
		// 登録処理は後述のServiceで実行
		if (result.hasErrors()) {
			return "register";
		}

		//パスワードと確認用パスワードが一致するか確認
		if (!form.getPassword().equals(form.getConfirmPassword())) {
			result.rejectValue("confirmPasswor", "error.confirmPassword", "パスワードが一致しません。");
			return "/register";
		}

		//DTO → Entity へ変換
		User user = new User();
		user.setName(form.getName());
		user.setEmail(form.getEmail());
		user.setPasswordHash(form.getPassword());

		userService.register(user); //DB登録

		return "redirect:/login";

	}

	//ログアウト処理
	@PostMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate(); //セッション破棄
		return "redirect:/welcome";
	}

}
