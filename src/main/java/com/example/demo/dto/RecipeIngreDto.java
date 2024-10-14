package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeIngreDto {

	private Integer ingredientId;
	
	private Integer recipeId;
	
	private String ingredientUnit; // 食材單位
	
	private Integer ingredientVolume; // 食材數量
	
}
