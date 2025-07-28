package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.CategoryDisplayDto;
import com.example.demo.entity.DefaultCategory;
import com.example.demo.entity.User;
import com.example.demo.entity.UserCategory;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.DefaultCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;
	private final DefaultCategoryRepository defaultCategoryRepository;
	
	public List<UserCategory> findByUser(User user){
		return categoryRepository.findByUser(user);
	}

	public List<CategoryDisplayDto> getAllForDisplay(User user) {
		List<CategoryDisplayDto> result = new ArrayList<>();
	
		//ユーザー固有カテゴリ
		List<UserCategory> userCategories = categoryRepository.findByUser(user);
		for (UserCategory uc : userCategories) {
			result.add(new CategoryDisplayDto(uc.getId(), uc.getName(), false));
		}

		//共通カテゴリ
		List<DefaultCategory> defaultCategories = defaultCategoryRepository.findAll();
		for (DefaultCategory dc : defaultCategories) {
			result.add(new CategoryDisplayDto(dc.getId(), dc.getName(), true));
		}

		//	//全カテゴリを取得（ユーザーIDに絞るなども後で追加可能）
		//	public List<UserCategory> findAll() {
		//		return categoryRepository.findAll();
		//	}
		//
		//	public List<UserCategory> findByUser(User user) {
		//		return categoryRepository.findByUser(user);
		//	}
		return result;

	}

	//IDからカテゴリ名を取得（default_ と user_ 両方探す）
	public String getCategoryNameByIdIncludingDefault(Long id, boolean isCategoryIsDefault) {
		if(isCategoryIsDefault) {
			return defaultCategoryRepository.findById(id)
				.map(DefaultCategory::getName)
				.orElse("不明");
		}else {
			return categoryRepository.findById(id)
				.map(UserCategory::getName)
				.orElse("不明");
		}
	}
	

	//登録時:default_ のIDだったらユーザーカテゴリにコピー（既存がなければ）
	public UserCategory resolveOrCopy(User user, Long id) {
		return categoryRepository.findById(id).orElseGet(() -> {
			DefaultCategory dc = defaultCategoryRepository.findById(id).orElse(null);
			if (dc == null)
				return null;

			//すでに同名が登録済みかチェック
			return categoryRepository.findByUserAndName(user, dc.getName())
					.orElseGet(() -> {
						UserCategory newCategory = UserCategory.builder()
								.user(user)
								.name(dc.getName())
								.build();
						return categoryRepository.save(newCategory);
					});
		});
	}

	
	//	//IDからカテゴリ名を取得
	//	public String getCategoryNameById(Long id) {
	//		return categoryRepository.findById(id)
	//				.map(UserCategory::getName)
	//				.orElse(null);
	//	}
	
	//カテゴリの保存を追加
	public UserCategory save(UserCategory category) {
		return categoryRepository.save(category);
	}
	
	//ユーザーと名前からカテゴリ取得
	public UserCategory findByUserAndName(User user, String name) {
		return categoryRepository.findByUserAndName(user,name).orElse(null);
	}
	
	//ユーザーとIDからカテゴリ取得
	public UserCategory findByUserAndId(User user, Long categoryId) {
		return categoryRepository.findByUserAndId(user,categoryId).orElse(null);
	}
	
	//カテゴリ削除操作
	public void deleteById(Long id) {
		categoryRepository.deleteById(id);
	}
	
	

}
