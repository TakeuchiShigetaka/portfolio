package com.example.demo.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.CategoryDisplayDto;
import com.example.demo.entity.User;
import com.example.demo.entity.UserCategory;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ExpenseService;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CategoryManageController {
	
	private final CategoryService categoryService;
	private final ExpenseService expenseService;
	private final UserService userService;
	
	//管理画面の表示
	@GetMapping("/category-manage")
	public String showCategoryManage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		
		User user = userService.findByEmail(userDetails.getUsername());
		
		//UserCategory → DTOに変換し、default_を除く形で渡す
		List<UserCategory> userCategories = categoryService.findByUser(user);
		List<CategoryDisplayDto> userCategoryDtos = CategoryDisplayDto.fromUserCategories(userCategories);
		
		model.addAttribute("userCategories", userCategoryDtos);
		model.addAttribute("newCategory", new UserCategory());
		model.addAttribute("user", user);
		
		return "category-manage";

	}
	

	//入力画面・出力画面から「ホーム画面に戻る」ボタンで遷移
	@GetMapping("/category-cancel")
	public String cancel() {
		return "redirect:/home";
	}
	
	//カテゴリの追加
	@PostMapping("/category-add")
	public String addCategory(@ModelAttribute("newCategory") UserCategory newCategory,
							  @AuthenticationPrincipal UserDetails userDetails) {
	User user = userService.findByEmail(userDetails.getUsername());
	newCategory.setUser(user);
	categoryService.save(newCategory);
	return "redirect:/category-manage";
	}
	
	//カテゴリの削除（支出で使われていない場合のみ）
	@PostMapping("category-delete/{id}")
	public String deleteCategory(@PathVariable Long id,
								 @AuthenticationPrincipal UserDetails userDetails,
								 RedirectAttributes redirectAttributes) {
		User user = userService.findByEmail(userDetails.getUsername());
		if(!expenseService.existsByUserAndCategoryId(user, id)) {
			categoryService.deleteById(id);
			redirectAttributes.addFlashAttribute("successMessage", "カテゴリを削除しました。");
		}else {
			redirectAttributes.addFlashAttribute("errorMessage", "このカテゴリは支出に使用されているため削除できません。");
		}
		
		return "redirect:/category-manage";
	}
	
}
