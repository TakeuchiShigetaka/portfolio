package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.Form.MonthlyGoalEditForm;
import com.example.demo.Form.MonthlyGoalEditForm.CategoryGoalDto;
import com.example.demo.entity.DefaultCategory;
import com.example.demo.entity.MonthlyCategoryGoals;
import com.example.demo.entity.MonthlyGoal;
import com.example.demo.entity.User;
import com.example.demo.entity.UserCategory;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.DefaultCategoryRepository;
import com.example.demo.repository.MonthlyCategoryGoalsRepository;
import com.example.demo.repository.MonthlyGoalRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MonthlyGoalQueryService {

	private final MonthlyGoalRepository monthlyGoalRepository;
	private final MonthlyCategoryGoalsRepository monthlyCategoryGoalsRepository;
	private final CategoryRepository categoryRepository;
	private final DefaultCategoryRepository defaultCategoryRepository;

	public MonthlyGoalEditForm buildForm(User user, int year, int month) {
		MonthlyGoalEditForm form = new MonthlyGoalEditForm();
		form.setYear(year);
		form.setMonth(month);

		//月全体の目標額（あれば）
		MonthlyGoal monthlyGoal = monthlyGoalRepository.findByUserAndYearAndMonth(user, year, month).orElse(null);
		if (monthlyGoal != null) {
			form.setTotalGoalAmount(monthlyGoal.getGoalAmount());
		}

		List<CategoryGoalDto> dtoList = new ArrayList<>();

		//既存のカテゴリ目標リストを一括取得（User + Default 両対応）
		List<MonthlyCategoryGoals> categoryGoals = monthlyCategoryGoalsRepository
				.findByUserOrDefault(user, year, month);
		//categoryIsDefault=true のものには DefaultCategory を手動でセット
		for(MonthlyCategoryGoals goal : categoryGoals) {
			if(goal.isCategoryIsDefault()) {
				Long defaultCategoryId = goal.getCategoryId();
				DefaultCategory dc = defaultCategoryRepository.findById(defaultCategoryId).orElse(null);
				goal.setDefaultCategory(dc);
			}
		}

		//1. ユーザーカテゴリから DTO 作成
		List<UserCategory> userCategories = categoryRepository.findByUser(user);
		for (UserCategory category : userCategories) {
			MonthlyCategoryGoals matched = categoryGoals.stream()
					.filter(goal -> !goal.isCategoryIsDefault() && goal.getUserCategory() != null)
					.filter(goal -> goal.getUserCategory().getId().equals(category.getId()))
					.findFirst()
					.orElse(null);

			CategoryGoalDto dto = new CategoryGoalDto();
			dto.setCategoryId(category.getId());
			dto.setCategoryName(category.getName());
			dto.setGoalAmount(matched != null ? matched.getGoalAmount() : null);
			dto.setDefault(false);

			dtoList.add(dto);
		}

		//2. デフォルトカテゴリから DTO 作成
		List<DefaultCategory> defaultCategories = defaultCategoryRepository.findAll();
		for (DefaultCategory category : defaultCategories) {
			MonthlyCategoryGoals matched = categoryGoals.stream()
					.filter(goal -> goal.isCategoryIsDefault() && goal.getDefaultCategory() != null)
					.filter(goal -> goal.getDefaultCategory().getId().equals(category.getId()))
					.findFirst()
					.orElse(null);

			CategoryGoalDto dto = new CategoryGoalDto();
			dto.setCategoryId(category.getId());
			dto.setCategoryName(category.getName());
			dto.setGoalAmount(matched != null ? matched.getGoalAmount() : null);
			dto.setDefault(true);

			dtoList.add(dto);

		}
	//DTOリストをフォームにセット
	form.setCategoryGoals(dtoList);return form;
}}
