package com.example.demo.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.entity.DefaultCategory;
import com.example.demo.entity.UserCategory;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryDisplayDto {
	
	private Long id; //user_category または default_category の ID
	private String name; //表示名 (例：食費)
	private boolean isDefault; //default か user_ かを区別するためのフラグ
	
	//ユーザーカテゴリ → DTOリスト変換
	public static List<CategoryDisplayDto> fromUserCategories(List<UserCategory> userCategories){
		return userCategories.stream()
				.map(c -> new CategoryDisplayDto(c.getId(), c.getName(), false))
				.collect(Collectors.toList());
	}
	
	//デフォルトカテゴリ → DTOリスト変換
	public static List<CategoryDisplayDto> fromDefaultCategories(List<DefaultCategory> defaultCategories){
		return defaultCategories.stream()
				.map(c -> new CategoryDisplayDto(c.getId(), c.getName(), true))
				.collect(Collectors.toList());
	}
	

}
