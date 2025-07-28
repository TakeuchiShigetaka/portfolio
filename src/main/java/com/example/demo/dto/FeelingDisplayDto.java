package com.example.demo.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.entity.DefaultFeeling;
import com.example.demo.entity.UserFeeling;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeelingDisplayDto {
	
	private Long id; //user_feeling または fefault_feeling の ID
	private String name; //表示名（例：満足）
	private String polarity; //極性 positive または negative のみ
	private boolean isDefault; //default_ か user_かを区別するためのフラグ

	
	//ユーザー感情 → DTOリスト変換
	public static List<FeelingDisplayDto> fromUserFeelings(List<UserFeeling> userFeelings){
		return userFeelings.stream()
				.map(f -> new FeelingDisplayDto(f.getId(), f.getName(),f.getPolarity(), false))
				.collect(Collectors.toList());
	}
	
	//デフォルト感情 → DTOリスト変換
	public static List<FeelingDisplayDto> fromDefaultFeelings(List<DefaultFeeling> defaultFeelings){
		return defaultFeelings.stream()
				.map(f -> new FeelingDisplayDto(f.getId(), f.getName(),f.getPolarity(), true))
				.collect(Collectors.toList());
	}
}
