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

import com.example.demo.dto.FeelingDisplayDto;
import com.example.demo.entity.User;
import com.example.demo.entity.UserFeeling;
import com.example.demo.service.ExpenseService;
import com.example.demo.service.FeelingService;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FeelingManageController {
	
	private final FeelingService feelingService;
	private final ExpenseService expenseService;
	private final UserService userService;
	
	//管理画面の表示
	@GetMapping("/feeling-manage")
	public String showFeelingManage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		
		User user = userService.findByEmail(userDetails.getUsername());
		
		//UserFeeling → DTOに変換し、default_を除く形で渡す
		List<UserFeeling> userFeelings = feelingService.findByUser(user);
		List<FeelingDisplayDto> userFeelingDtos = FeelingDisplayDto.fromUserFeelings(userFeelings);
		
		model.addAttribute("userFeelings", userFeelingDtos);
		model.addAttribute("newFeeling", new UserFeeling());
		model.addAttribute("user", user);
		
		return "feeling-manage";
	}
	
	//入力画面・出力画面から「ホーム画面に戻る」ボタンで遷移
	@GetMapping("/feeling-cancel")
	public String cancel() {
		return "redirect:/home";
	}
	
	//感情の追加
	@PostMapping("/feeling-add")
	public String addFeeling(@ModelAttribute("newFeeling") UserFeeling newFeeling,
							 @AuthenticationPrincipal UserDetails userDetails) {
		User user = userService.findByEmail(userDetails.getUsername());
		newFeeling.setUser(user);
		feelingService.save(newFeeling);
		return "redirect:/feeling-manage";
	}
	
	//カテゴリの削除（支出で使われていない場合のみ）
	@PostMapping("/feeling-delete/{id}")
	public String deleteFeeling(@PathVariable Long id,
								@AuthenticationPrincipal UserDetails userDetails,
								RedirectAttributes redirectAttributes) {
		User user = userService.findByEmail(userDetails.getUsername());
		if(!expenseService.existsByUserAndFeelingId(user, id)) {
			feelingService.deleteById(id);
			redirectAttributes.addFlashAttribute("successMessage", "感情を削除しました。");
		}else {
			redirectAttributes.addFlashAttribute("errorMessage", "この感情は支出に使用されているため削除できません。");
		}
		
		return "redirect:/feeling-manage";
	}
	

}
