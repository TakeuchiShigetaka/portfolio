package com.example.demo.entity;

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
@Table(name= "user_feelings")
public class UserFeeling extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable = false)
	private Long Id;
	
	@ManyToOne
	@JoinColumn(name="user_id", nullable = false) //(FK)外部キーのカラム名
	private User user;
	
	@Column(name="name", nullable = false)
	private String name;
	
	@Column(name="polarity", nullable = false)
	private String polarity;	//"positive"　または　"negative"
	
	// createdAt, updatedAt は BaseEntity から継承

}
