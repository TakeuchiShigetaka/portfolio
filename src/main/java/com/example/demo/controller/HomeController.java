package com.example.demo.controller;

import java.time.LocalDate;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.dto.HomeSummaryDto;
import com.example.demo.entity.User;
import com.example.demo.service.HomeSummaryService;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

	private final UserService userService;
	private final HomeSummaryService homeSummaryService;
	
	//ホーム画面表示
	@GetMapping("/home")
	public String home(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		
		User user = userService.findByEmail(userDetails.getUsername());
		LocalDate now = LocalDate.now();
		int year = now.getYear();
		int month = now.getMonthValue();
		
		HomeSummaryDto summary = homeSummaryService.getSummary(user, year, month);
		model.addAttribute("summary", summary);
	    model.addAttribute("user", user);
	    return "home";
	}
}
