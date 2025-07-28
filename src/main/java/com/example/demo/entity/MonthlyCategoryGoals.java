package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "monthly_category_goals")
public class MonthlyCategoryGoals extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = true) //userがnullでも保存できる。user_idのないデフォルトカテゴリ項目も保存できるようにするため
	@JoinColumn(name = "user_id", nullable = true)
	private User user;

	@Column(name = "year", nullable = false)
	private int year;

	@Column(name = "month", nullable = false)
	private int month;

	//読み取り専用のカテゴリ参照
	@ManyToOne
	@JoinColumn(name = "category_id", insertable = false, updatable = false)
	private UserCategory userCategory;

	@Transient
	private DefaultCategory defaultCategory;
	
	////デフォルトカテゴリをサービスクラスで取得するキーdefaultCategoryIdをセット
	@Transient
	private Long defaultCategoryId;
	
	public Long getDefaultCategoryId() {
		return defaultCategory != null ? defaultCategory.getId() : defaultCategoryId; 
	}
	
	public void setDefaultCategoryId(Long defaultCategoryId) {
		this.defaultCategoryId = defaultCategoryId;
	}
	////↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	@Column(name = "category_id", nullable = false, insertable= true, updatable= true )
	private Long categoryId;

	@Column(name = "category_is_default", nullable = false)
	private boolean categoryIsDefault;

	@Column(name = "goal_amount", nullable = false)
	private int goalAmount;

	//カテゴリ名を返すラッパー
	public String getResolvedCategoryName() {
		return categoryIsDefault
				? (defaultCategory != null ? defaultCategory.getName() : null)
				: (userCategory != null ? userCategory.getName() : null);
	}

	// createdAt, updatedAt は BaseEntity から継承

}
