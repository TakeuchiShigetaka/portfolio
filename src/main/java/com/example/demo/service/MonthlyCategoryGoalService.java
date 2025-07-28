package com.example.demo.service;

import java.util.Optional;

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
public class MonthlyCategoryGoalService {

	private final MonthlyGoalRepository monthlyGoalRepository;
	private final MonthlyCategoryGoalsRepository monthlyCategoryGoalsRepository;
	private final DefaultCategoryRepository defaultCategoryRepository;
	private final CategoryRepository categoryRepository;

	public void saveOrUpdateMonthlyGoal(User user, MonthlyGoalEditForm form) {

		//1.月全体の目標額(MonthlyGoal)を保存 or 更新
		Optional<MonthlyGoal> optionalGoal = monthlyGoalRepository.findByUserIdAndYearAndMonth(user.getId(),
				form.getYear(), form.getMonth());

		MonthlyGoal monthlyGoal = optionalGoal.orElseGet(() -> MonthlyGoal.builder()
				.user(user)
				.year(form.getYear())
				.month(form.getMonth())
				.build());

		monthlyGoal.setGoalAmount(form.getTotalGoalAmount() != null ? form.getTotalGoalAmount() : 0);
		monthlyGoalRepository.save(monthlyGoal);

		//2.カテゴリ別目標額(MonthlyCategoryGoals)を保存 or 更新 
		for (CategoryGoalDto dto : form.getCategoryGoals()) {
			Optional<MonthlyCategoryGoals> optionalCategoryGoal;
			
			if(dto.isDefault()) {
				//デフォルトカテゴリ → user は null
				optionalCategoryGoal = monthlyCategoryGoalsRepository
					.findByUserIsNullAndYearAndMonthAndCategoryId(form.getYear(), form.getMonth(),dto.getCategoryId());
			}else {
				//ユーザーカテゴリ → user を使う
				optionalCategoryGoal = monthlyCategoryGoalsRepository
					.findByUserAndYearAndMonthAndCategoryId(user, form.getYear(), form.getMonth(), dto.getCategoryId());
			}

			MonthlyCategoryGoals categoryGoal = optionalCategoryGoal.orElseGet(() -> MonthlyCategoryGoals.builder()
					.year(form.getYear())
					.month(form.getMonth())
					.categoryId(dto.getCategoryId())
					.categoryIsDefault(dto.isDefault())
					.build());
			
			if(dto.isDefault()) {
				//DefaultCategoryをセット
				DefaultCategory defaultCat = defaultCategoryRepository.findById(dto.getCategoryId()).orElse(null);
					categoryGoal.setDefaultCategory(defaultCat);
					categoryGoal.setUserCategory(null);
			}else {
				//UserCategoryをセット
				UserCategory userCat = categoryRepository.findById(dto.getCategoryId()).orElse(null);
					categoryGoal.setUserCategory(userCat);
					categoryGoal.setDefaultCategory(null);
			}

			categoryGoal.setUser(dto.isDefault() ? null : user);
			Integer amount = (dto.getGoalAmount() != null) ? dto.getGoalAmount() : 0;
			categoryGoal.setGoalAmount(amount);
			System.out.println("Saving default category goal: " + categoryGoal);
			monthlyCategoryGoalsRepository.save(categoryGoal);
		}

	}

}
