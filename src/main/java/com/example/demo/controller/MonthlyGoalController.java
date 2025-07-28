package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Form.MonthlyGoalEditForm;
import com.example.demo.dto.GoalSummaryDto;
import com.example.demo.dto.MonthlyPolarityCountDto;
import com.example.demo.entity.User;
import com.example.demo.service.GoalSummaryService;
import com.example.demo.service.MonthlyCategoryGoalService;
import com.example.demo.service.MonthlyGoalQueryService;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MonthlyGoalController {

	private final UserService userService;
	private final MonthlyCategoryGoalService monthlyCategoryGoalService;
	private final MonthlyGoalQueryService monthlyGoalQueryService;
	private final GoalSummaryService goalSummaryService;

	//月別目標額設定画面を表示
	@GetMapping("monthly-goal")
	public String showMonthlyGoals(
			@AuthenticationPrincipal UserDetails userDetails,
			@RequestParam(name = "year", required = false) Integer year,
			@RequestParam(name = "month", required = false) Integer month,
			Model model) {

		User user = userService.findByEmail(userDetails.getUsername());

		LocalDate now = LocalDate.now();
		int targetYear = (year != null) ? year : now.getYear();
		int targetMonth = (month != null) ? month : now.getMonthValue();

		//月全体の目標額・カテゴリの目標額を取得（Serviceにまとめる）
		MonthlyGoalEditForm form = monthlyGoalQueryService.buildForm(user, targetYear, targetMonth);

		model.addAttribute("form", form);
		model.addAttribute("year", targetYear);
		model.addAttribute("month", targetMonth);

		return "monthly-goal-edit";
	}

	//目標額をセーブ、その後リダイレクト
	@PostMapping("/monthly-goal")
	public String saveMonthlyGoals(
			@AuthenticationPrincipal UserDetails userDetails,
			@ModelAttribute MonthlyGoalEditForm form) {

		User user = userService.findByEmail(userDetails.getUsername());

		monthlyCategoryGoalService.saveOrUpdateMonthlyGoal(user, form);

		//PRGパターン：登録後に再度同じ年月に戻る
		return String.format("redirect:/monthly-goal?year=%d&month=%d", form.getYear(), form.getMonth());
	}
	
	

	//年間支出統計リストへ遷移
	@GetMapping("/goals-summary")
	public String showGoalSummary(
			@AuthenticationPrincipal UserDetails userDetails,
			@RequestParam(name = "year", required = false) Integer year,
			Model model) {

		User user = userService.findByEmail(userDetails.getUsername());

		int targetYear = (year != null) ? year : LocalDate.now().getYear();

		List<GoalSummaryDto> summaryList = goalSummaryService.getGoalSummaryForYear(user, targetYear);
		Map<Integer, MonthlyPolarityCountDto> polarityMap = goalSummaryService.getMonthlyPolarityCountMap(user, targetYear);
		Map<String, Long> totalPolarityCounts = goalSummaryService.countTotalFeelingsByPolarity(user, targetYear);

		model.addAttribute("user", user);
		model.addAttribute("summaryList", summaryList);
		model.addAttribute("monthlyPolarityMap", polarityMap);
		model.addAttribute("totalPolarities", totalPolarityCounts);
		model.addAttribute("year", targetYear);

		return "goals-summary";

	}

	//キャンセル、yearを保持して遷移する
	@GetMapping("/monthly-goal-cancel")
	public String cancelToMonthlyGoal(
			@RequestParam(name = "year", required = false) Integer year,
			@RequestParam(name = "month", required = false) Integer month) {

		int targetYear = (year != null) ? year : LocalDate.now().getYear();
		int targetMonth = (month != null) ? month : LocalDate.now().getMonthValue();

		//もとの月別表示画面へ戻る
		return String.format("redirect:/monthly-goals?year=%d&month=%d", targetYear, targetMonth);
	}

}
