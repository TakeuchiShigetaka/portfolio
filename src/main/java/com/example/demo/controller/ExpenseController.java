package com.example.demo.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Form.ExpenseForm;
import com.example.demo.dto.CategoryDisplayDto;
import com.example.demo.dto.ExpenseWithPolarityDto;
import com.example.demo.dto.FeelingDisplayDto;
import com.example.demo.dto.HomeSummaryDto;
import com.example.demo.entity.Expense;
import com.example.demo.entity.User;
import com.example.demo.entity.UserCategory;
import com.example.demo.entity.UserFeeling;
import com.example.demo.service.CategoryService;
import com.example.demo.service.DefaultCategoryService;
import com.example.demo.service.DefaultFeelingService;
import com.example.demo.service.ExpenseService;
import com.example.demo.service.FeelingService;
import com.example.demo.service.HomeSummaryService;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ExpenseController {

	private final CategoryService categoryService;
	private final FeelingService feelingService;
	private final UserService userService;
	private final ExpenseService expenseService;
	private final DefaultCategoryService defaultCategoryService;
	private final DefaultFeelingService defaultFeelingService;
	private final HomeSummaryService homeSummaryService;

	//支出入力画面の表示
	@GetMapping("/expense-new")
	public String showExpenseForm(@ModelAttribute ExpenseForm form,
			@AuthenticationPrincipal UserDetails userDetails,
			Model model) {

		//ログイン中のユーザー情報を取得
		User user = userService.findByEmail(userDetails.getUsername());

		//カテゴリと感情を統一してDtoで取得
		List<CategoryDisplayDto> categories = new ArrayList<>();
		categories.addAll(CategoryDisplayDto.fromUserCategories(categoryService.findByUser(user)));
		categories.addAll(CategoryDisplayDto.fromDefaultCategories(defaultCategoryService.findAll()));

		List<FeelingDisplayDto> feelings = new ArrayList<>();
		feelings.addAll(FeelingDisplayDto.fromUserFeelings(feelingService.findByUser(user)));
		feelings.addAll(FeelingDisplayDto.fromDefaultFeelings(defaultFeelingService.findAll()));

		//モデルに渡す
		model.addAttribute("categories", categories);
		model.addAttribute("feelings", feelings);

		return "expense-form";

	}

	//入力画面・出力画面から「ホーム画面に戻る」ボタンで遷移
	@GetMapping("/expense-cancel")
	public String cancel() {
		return "redirect:/home";
	}

	//支出確認画面の表示
	@PostMapping("/expense-confirm")
	public String showConfirmPage(
			@Validated @ModelAttribute("expenseForm") ExpenseForm form,
			BindingResult result,
			Model model,
			@AuthenticationPrincipal UserDetails userDetails) {

		//ログイン中のユーザー情報取得
		User user = userService.findByEmail(userDetails.getUsername());

		//入力画面に戻す
		if (result.hasErrors()) {
			List<CategoryDisplayDto> categories = new ArrayList<>();
			categories.addAll(CategoryDisplayDto.fromUserCategories(categoryService.findByUser(user)));
			categories.addAll(CategoryDisplayDto.fromDefaultCategories(defaultCategoryService.findAll()));

			List<FeelingDisplayDto> feelings = new ArrayList<>();
			feelings.addAll(FeelingDisplayDto.fromUserFeelings(feelingService.findByUser(user)));
			feelings.addAll(FeelingDisplayDto.fromDefaultFeelings(defaultFeelingService.findAll()));

			return "expense-form";
		}

		//カテゴリと感情名を取得
		boolean isCategoryIsDefault = categoryService.findByUserAndId(user, form.getCategoryId()) == null;
		boolean isFeelingIsDefault = feelingService.findByUserAndId(user, form.getFeelingId()) == null;
		
		String categoryName = categoryService.getCategoryNameByIdIncludingDefault(form.getCategoryId(),isCategoryIsDefault);
		String feelingName = feelingService.getFeelingNameByIdIncludingDefault(form.getFeelingId(), isFeelingIsDefault);

		model.addAttribute("categoryName", categoryName);
		model.addAttribute("feelingName", feelingName);
		model.addAttribute("expenseForm", form);

		return "expense-confirm";

	}

	//新規支出登録
	@PostMapping("/expense-save")
	public String saveExpense(@ModelAttribute("expenseForm") ExpenseForm form,
			@AuthenticationPrincipal UserDetails userDetails,
			RedirectAttributes redirectAttributes ) {

		//ログイン中のユーザー情報取得
		User user = userService.findByEmail(userDetails.getUsername());

		//IDに応じてUserCategoryまたはコピーされたカテゴリを取得
		//カテゴリ一覧を取得（ログインした人が登録したものだけ）default_のidならコピーしてくる
		//		UserCategory category = categoryService.resolveOrCopy(user, form.getCategoryId());

		UserCategory userCategory = categoryService.findByUserAndId(user, form.getCategoryId());
		boolean isDefaultCategory = (userCategory == null);

		//IDに応じてUserFeelingまたはコピーされたカテゴリを取得
		//感情一覧を取得（ログインした人が登録したものだけ）default_のidならコピーしてくる
		//		UserFeeling feeling = feelingService.resolveOrCopy(user, form.getFeelingId());

		UserFeeling userFeeling = feelingService.findByUserAndId(user, form.getFeelingId());
		boolean isDefaultFeeling = (userFeeling == null);

		//入力されたIDと一致するカテゴリを探す
		//エンティティに変換して登録
		Expense expense = Expense.builder()
				.user(user) //支出登録するユーザー user_id
				.amount(form.getAmount()) //支出額
				.memo(form.getMemo())
				.categoryId(form.getCategoryId())//記録するカテゴリ 食費、日用品など category_id
				.categoryIsDefault(isDefaultCategory)
				.feelingId(form.getFeelingId()) //記録する感情 満足、公開など feeling_id
				.feelingIsDefault(isDefaultFeeling)
				.date(LocalDate.parse(form.getDate())) //支出日
				.build();

		expenseService.save(expense);
		
		redirectAttributes.addFlashAttribute("successMessage", "支出を新規登録しました。");

		return "redirect:/home";
	}

	//支出一覧画面へ遷移
	@GetMapping("/expense-list")
	public String showExpenseList(
			@RequestParam(value = "year", required = false) Integer year,
			@RequestParam(value = "month", required = false) Integer month,
			@AuthenticationPrincipal UserDetails userDetails,
			Model model) {

		User user = userService.findByEmail(userDetails.getUsername());

		//現在月（未指定時）
		LocalDate now = LocalDate.now();
		int targetYear = (year != null) ? year : now.getYear();
		int targetMonth = (month != null) ? month : now.getMonthValue();

		LocalDate start = LocalDate.of(targetYear, targetMonth, 1);
		LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

		List<Expense> expenses = expenseService.findByUserAndDateBetween(user, start, end);

		List<ExpenseWithPolarityDto> expenseDtos = expenses.stream()
				.filter(expense -> expense.getDate() != null) //null除外
				.sorted((e1, e2) -> e1.getDate().compareTo(e2.getDate())) //日付昇順ソート
				.map(expense -> {
					String polarity;

					if (expense.isFeelingIsDefault()) {
						polarity = expense.getDefaultFeeling() != null
								? expense.getDefaultFeeling().getPolarity()
								: "不明";
					} else {
						polarity = expense.getUserFeeling() != null
								? expense.getUserFeeling().getPolarity()
								: "不明";
					}

					return new ExpenseWithPolarityDto(expense, polarity);
				}).collect(Collectors.toList());

		HomeSummaryDto summary = homeSummaryService.getSummary(user, targetYear, targetMonth);
		model.addAttribute("summary", summary);
		model.addAttribute("expenses", expenseDtos);
		model.addAttribute("targetYear", targetYear);
		model.addAttribute("targetMonth", targetMonth);
		model.addAttribute("user", user);

		return "expense-list";
	}

	//支出編集画面へ遷移
	@GetMapping("/expense-edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model,
			@AuthenticationPrincipal UserDetails userDetails) {

		User user = userService.findByEmail(userDetails.getUsername());
		Expense expense = expenseService.findByIdAndUser(id, user);

		if (expense == null) {
			return "return:/expense-list";
		}

		ExpenseForm form = new ExpenseForm();
		form.setAmount(expense.getAmount());
		form.setMemo(expense.getMemo());
		form.setCategoryId(expense.getCategoryId());
		form.setFeelingId(expense.getFeelingId());
		form.setDate(expense.getDate().toString());

		//カテゴリ・感情リストは入力画面と同様に用意
		List<CategoryDisplayDto> categories = new ArrayList<>();
		categories.addAll(CategoryDisplayDto.fromUserCategories(categoryService.findByUser(user)));
		categories.addAll(CategoryDisplayDto.fromDefaultCategories(defaultCategoryService.findAll()));

		List<FeelingDisplayDto> feelings = new ArrayList<>();
		feelings.addAll(FeelingDisplayDto.fromUserFeelings(feelingService.findByUser(user)));
		feelings.addAll(FeelingDisplayDto.fromDefaultFeelings(defaultFeelingService.findAll()));

		model.addAttribute("expenseForm", form);
		model.addAttribute("categories", categories);
		model.addAttribute("feelings", feelings);
		model.addAttribute("expenseId", id);

		return "expense-edit-form"; //編集用のテンプレート
	}

	//支出編集確認画面へ遷移
	@PostMapping("/expense-edit-confirm/{id}")
	public String showEditConfirm(@PathVariable Long id,
			@Validated @ModelAttribute ExpenseForm form,
			BindingResult result,
			@AuthenticationPrincipal UserDetails userDetails,
			Model model) {
		
		User user = userService.findByEmail(userDetails.getUsername());
		
		if(result.hasErrors()) {
			
			List<CategoryDisplayDto> categories = new ArrayList<>();
			categories.addAll(CategoryDisplayDto.fromUserCategories(categoryService.findByUser(user)));
			categories.addAll(CategoryDisplayDto.fromDefaultCategories(defaultCategoryService.findAll()));

			List<FeelingDisplayDto> feelings = new ArrayList<>();
			feelings.addAll(FeelingDisplayDto.fromUserFeelings(feelingService.findByUser(user)));
			feelings.addAll(FeelingDisplayDto.fromDefaultFeelings(defaultFeelingService.findAll()));
			
			model.addAttribute("categories", categories);
			model.addAttribute("feelings", feelings);
			return "expense-edit-form";
		}
		
		//修正前の支出を取得
		Expense before = expenseService.findByIdAndUser(id, user);
		if(before == null)return "redirect:/expense-list";
		
		//修正後の情報を仮のExpenseに詰めてDTO化する
		Expense after = Expense.builder()
			.amount(form.getAmount())
			.memo(form.getMemo())
			.categoryId(form.getCategoryId())
			.feelingId(form.getFeelingId())
			.date(LocalDate.parse(form.getDate()))
			.categoryIsDefault(categoryService.findByUserAndId(user, form.getCategoryId()) == null)
			.feelingIsDefault(feelingService.findByUserAndId(user, form.getFeelingId()) == null)
			.build();
		
		boolean isCategoryIsDefault = categoryService.findByUserAndId(user, form.getCategoryId()) == null;
		boolean isFeelingIsDefault = feelingService.findByUserAndId(user, form.getFeelingId()) == null;
//		String polarity = feelingService.getFeelingPolarityByIdIncludingDefault(form.getFeelingId(), isFeelingIsDefault);
		
		//VIEW表示のためカテゴリ、感情オブジェクトを取得
		if(isCategoryIsDefault) {
			after.setDefaultCategory(defaultCategoryService.findById(form.getCategoryId()).orElse(null));
		}else {
			after.setUserCategory(categoryService.findByUserAndId(user, form.getCategoryId()));
		}
		
		if(isFeelingIsDefault) {
			after.setDefaultFeeling(defaultFeelingService.findById(form.getFeelingId()).orElse(null));
		}else {
			after.setUserFeeling(feelingService.findByUserAndId(user, form.getFeelingId()));
		}
		
		
		//polarity（極性）の取得
		String beforePolarity = before.isFeelingIsDefault() && before.getDefaultFeeling() != null
				? before.getDefaultFeeling().getPolarity()
				: before.getUserFeeling() != null
					? before.getUserFeeling().getPolarity()
					: "不明";
		
		String afterPolarity = after.isFeelingIsDefault() && after.getDefaultFeeling() != null
				? after.getDefaultFeeling().getPolarity()
				: after.getUserFeeling() != null
					? after.getUserFeeling().getPolarity()
					: "不明";
		
		String categoryName = categoryService.getCategoryNameByIdIncludingDefault(form.getCategoryId(), isCategoryIsDefault);
		String feelingName = feelingService.getFeelingNameByIdIncludingDefault(form.getFeelingId(), isFeelingIsDefault);
		
		model.addAttribute("before", before);
		model.addAttribute("beforePolarity", beforePolarity);
		model.addAttribute("categoryName", categoryName);
		model.addAttribute("after", after);
		model.addAttribute("afterPolarity", afterPolarity);
		model.addAttribute("feelingName", feelingName);
		model.addAttribute("expenseId", id);
		
		return "expense-edit-confirm";
		
	}

	//支出編集確定（保存）expenseIdを指定して保存
	@PostMapping("/expense-edit-save/{id}")
	public String saveEditExpense(@PathVariable Long id,
			@Validated @ModelAttribute("expenseForm") ExpenseForm form,
			BindingResult result,
			@AuthenticationPrincipal UserDetails userDetails,
			Model model,
			RedirectAttributes redirectAttributes) {

		User user = userService.findByEmail(userDetails.getUsername());

		if (result.hasErrors()) {
			//編集画面に戻る際にカテゴリ・感情の再設定をする
			List<CategoryDisplayDto> categories = new ArrayList<>();
			categories.addAll(CategoryDisplayDto.fromUserCategories(categoryService.findByUser(user)));
			categories.addAll(CategoryDisplayDto.fromDefaultCategories(defaultCategoryService.findAll()));

			List<FeelingDisplayDto> feelings = new ArrayList<>();
			feelings.addAll(FeelingDisplayDto.fromUserFeelings(feelingService.findByUser(user)));
			feelings.addAll(FeelingDisplayDto.fromDefaultFeelings(defaultFeelingService.findAll()));

			model.addAttribute("categories", categories);
			model.addAttribute("feelings", feelings);
			return "expense-edit-form";
		}

		Expense expense = expenseService.findByIdAndUser(id, user);
		if (expense == null) {
			return "redirect:/expense-list";
		}

		//フォームの値で更新
		expense.setAmount(form.getAmount());
		expense.setMemo(form.getMemo());
		expense.setCategoryId(form.getCategoryId());
		expense.setFeelingId(form.getFeelingId());
		expense.setDate(LocalDate.parse(form.getDate()));

		//デフォルトかどうかはカテゴリ・感情を取得して判定
		UserCategory userCategory = categoryService.findByUserAndId(user, form.getCategoryId());
		boolean isCategoryIsDefault = (userCategory == null);
		expense.setCategoryIsDefault(isCategoryIsDefault);
		if(isCategoryIsDefault) {
			expense.setDefaultCategory(defaultCategoryService.findById(form.getCategoryId())
			.orElse(null));
		}else {
			expense.setUserCategory(userCategory);
		}
		
		UserFeeling userFeeling = feelingService.findByUserAndId(user, form.getFeelingId());
		boolean isFeelingIsDefault = (userFeeling == null) ;
		expense.setFeelingIsDefault(isFeelingIsDefault);
		if(isFeelingIsDefault) {
			expense.setDefaultFeeling(defaultFeelingService.findById(form.getFeelingId())
			.orElse(null));
		}else {
			expense.setUserFeeling(userFeeling);
		}

		expenseService.save(expense);
		
		redirectAttributes.addFlashAttribute("successMessage", "支出を修正しました。");

		return "redirect:/expense-list";
	}

	//支出削除画面へ遷移
	@GetMapping("/expense-delete-confirm/{id}")
	public String showDeleteConfirm(@PathVariable Long id,
			@AuthenticationPrincipal UserDetails userDetails,
			Model model) {

		User user = userService.findByEmail(userDetails.getUsername());
		Expense expense = expenseService.findByIdAndUser(id, user);
		if (expense == null) {
			return "expense-list";
		}

		String polarity;
		if (expense.isFeelingIsDefault()) {
			polarity = expense.getDefaultFeeling() != null
					? expense.getDefaultFeeling().getPolarity()
					: "不明";
		} else {
			polarity = expense.getUserFeeling() != null
					? expense.getUserFeeling().getPolarity()
					: "不明";
		}

		model.addAttribute("expense", expense);
		model.addAttribute("polarity", polarity);
		return "expense-delete-confirm";
	}

	//削除実行
	@PostMapping("/expense-delete/{id}")
	public String deleteExpense(@PathVariable Long id,
			@AuthenticationPrincipal UserDetails userDetails,
			RedirectAttributes redirectAttributes) {

		User user = userService.findByEmail(userDetails.getUsername());
		Expense expense = expenseService.findByIdAndUser(id, user);
		if (expense != null) {
			expenseService.delete(expense);
		}
		redirectAttributes.addFlashAttribute("successMessage", "支出を削除しました。");
		
		return "redirect:/expense-list";
	}

}
