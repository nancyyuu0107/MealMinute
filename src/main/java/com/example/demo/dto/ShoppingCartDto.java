package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShoppingCartDto {
	private Integer customerId;

	private Integer ingredientId;

	private Integer quantity;
}
