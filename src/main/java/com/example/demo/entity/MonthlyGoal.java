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
@Table(name="monthly_goals")
public class MonthlyGoal extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name= "user_id", nullable= false)
	private User user;
	
	@Column(name= "year", nullable= false)
	private int year;
	
	@Column(name= "month", nullable= false)
	private int month;
	
	@Column(name="goal_amount", nullable= false)
	private int goalAmount;
	
	// createdAt, updatedAt は BaseEntity から継承

}
