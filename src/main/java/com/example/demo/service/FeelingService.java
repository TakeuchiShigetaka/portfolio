package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.FeelingDisplayDto;
import com.example.demo.entity.DefaultFeeling;
import com.example.demo.entity.User;
import com.example.demo.entity.UserFeeling;
import com.example.demo.repository.DefaultFeelingRepository;
import com.example.demo.repository.FeelingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeelingService {
	
	private final FeelingRepository feelingRepository;
	private final DefaultFeelingRepository defaultFeelingRepository;
	
	public List<UserFeeling> findByUser(User user){
		return feelingRepository.findByUser(user);
	}
	
	public List<FeelingDisplayDto> getAllForDisplay(User user){
		List<FeelingDisplayDto> result = new ArrayList<>();
		
		//ユーザー固有感情
		List<UserFeeling> userFeelings = feelingRepository.findByUser(user);
		for(UserFeeling uf : userFeelings) {
			result.add(new FeelingDisplayDto(uf.getId(),uf.getName(),uf.getPolarity(), false));
		}
		
		//共通感情
		List<DefaultFeeling> defaultFeelings = defaultFeelingRepository.findAll();
		for(DefaultFeeling df : defaultFeelings) {
			result.add(new FeelingDisplayDto(df.getId(), df.getName(),df.getPolarity(), true));
		}
		
//		//全感情を全件取得（ユーザーIDに絞るなども後で可能）
//		public List<UserFeeling> findAll(){
//			return feelingRepository .findAll();
//		}
	//	
//		public List<UserFeeling> findByUser(User user){
//			return feelingRepository.findByUser(user);
//		}
		
		return result;
 }
	
	//IDから感情名を取得（default_ と user_ をFeelingIsDefaultフィールドで判別して探す）
	public String getFeelingNameByIdIncludingDefault(Long id, boolean isFeelingIsDefault) {
		if(isFeelingIsDefault) {
			return defaultFeelingRepository.findById(id)
				.map(DefaultFeeling::getName)
				.orElse("不明");
		}else {
			return feelingRepository.findById(id)
				.map(UserFeeling::getName)
				.orElse("不明");
		}
	}
	
	//IDから極性名を取得（default_ と user_ をFeelingIsDefaultフィールドで判別して探す）
	public String getFeelingPolarityByIdIncludingDefault(Long id, boolean isFeelingIsDefault) {
		if(isFeelingIsDefault) {
			return defaultFeelingRepository.findById(id)
				.map(DefaultFeeling::getPolarity)
				.orElse("不明");
		}else {
			return feelingRepository.findById(id)
				.map(UserFeeling::getPolarity)
				.orElse("不明");
		}
	}
		
	//登録時:default_ のIDだったらユーザーカテゴリにコピー（既存がなければ）
	public UserFeeling resolveOrCopy(User user, Long id) {
		return feelingRepository.findById(id).orElseGet(() -> {
			DefaultFeeling df = defaultFeelingRepository.findById(id).orElse(null);
			if(df == null) return null;
			
			//すでに同名が登録済みかチェック
			return feelingRepository.findByUserAndName(user,df.getName())
					.orElseGet(() -> {
						UserFeeling newFeeling = UserFeeling.builder()
								.user(user)
								.name(df.getName())
								.polarity(df.getPolarity())
								.build();
						return feelingRepository.save(newFeeling);
					});
		});
	}

	
//	//IDから感情名を取得
//	public String getFeelingNameById(Long id) {
//		return feelingRepository.findById(id)
//				.map(UserFeeling::getName)
//				.orElse(null);
//	}
	
	//感情の保存を追加
	public UserFeeling save(UserFeeling feeling) {
		return feelingRepository.save(feeling);
	}
	
	//ユーザーと名前から感情取得
	public UserFeeling findByUserAndName(User user, String name) {
		return feelingRepository.findByUserAndName(user, name).orElse(null);
	}
	
	//ユーザーとIDから感情取得
	public UserFeeling findByUserAndId(User user, Long categoryId) {
		return feelingRepository.findByUserAndId(user,categoryId).orElse(null);
	}
	
	//カテゴリ削除操作
	public void deleteById(Long id) {
		feelingRepository.deleteById(id);
	}

}
