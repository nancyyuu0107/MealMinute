package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orderdetail")
public class OrderDetail {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer orderDetailId;

	private Integer quantity;

	private Integer totalAmount;

	@ManyToOne
	@JoinColumn(name = "orderId")
	@JsonBackReference("order-reference") // 使用自定義名稱
	private Orders orders;

	@ManyToOne
	@JoinColumn(name = "ingredientId")
	@JsonBackReference("ingredient-reference") // 使用自定義名稱
	private IngredientBean ingredient;
}
