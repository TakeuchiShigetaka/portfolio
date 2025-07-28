package com.example.demo.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="expenses")
public class Expense extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name= "user_id", nullable= false) //(FK)外部キーのカラム名
	private User user;
	
	@Column(name= "amount",nullable= false)
	private int amount;
	
//	@ManyToOne
//	@JoinColumn(name= "category_id",nullable= false) //(FK)外部キーのカラム名
//	private UserCategory category;
	
	//読み取り専用のカテゴリ参照
	
	@ManyToOne
	@JoinColumn(name= "category_id", insertable = false, updatable = false)
	private UserCategory userCategory;
	
	@ManyToOne
	@JoinColumn(name= "category_id", insertable = false, updatable = false)
	private DefaultCategory defaultCategory;
    
	@Column(name= "category_id",nullable= false)
	private Long categoryId;
	
	// ↓カテゴリIDと、それがdefaultかuserかを区別する
	@Column(name= "category_is_default", nullable= false)
	private boolean categoryIsDefault;
	
//	@ManyToOne
//	@JoinColumn(name= "feeling_id",nullable= false) //(FK)外部キーのカラム名
//	private UserFeeling feeling;
	
	//読み取り専用の感情
	@ManyToOne
	@JoinColumn(name = "feeling_id", insertable = false, updatable = false)
	private UserFeeling userFeeling;
	
	@ManyToOne
	@JoinColumn(name = "feeling_id", insertable = false, updatable = false)
	private DefaultFeeling defaultFeeling;
	
	@Column(name= "feeling_id",nullable= false)
	private Long feelingId;
	
	// ↓カテゴリIDと、それがdefaultかuserかを区別する
	@Column(name= "feeling_is_default", nullable= false )
	private boolean feelingIsDefault;
	
	@Column(name= "date", nullable= false)
	private LocalDate date;
	
	@Column(name= "memo", nullable= false)
	private String memo;
	
	//カテゴリ・感情名を返すラッパー
	public String getResolvedCategoryName() {
		return categoryIsDefault
				? (defaultCategory != null ? defaultCategory.getName() : null)
				:(userCategory != null ? userCategory.getName() : null);
	}
	
	public String getResolvedFeelingName() {
		return feelingIsDefault
				? (defaultFeeling != null ? defaultFeeling.getName() : null)
				: (userFeeling != null ? userFeeling.getName() : null);
	}

	
	// createdAt, updatedAt は BaseEntity から継承

}
