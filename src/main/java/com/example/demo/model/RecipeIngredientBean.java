package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "recipeIngredient")
public class RecipeIngredientBean {

	@EmbeddedId // 複合主鍵
	private RecipeIngredientId recipeIngreId;
	
	private String ingredientUnit; // 食材單位
		
	private Integer ingredientVolume; // 食材數量
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("ingredientId")
	@JsonManagedReference
	@JoinColumn(name = "ingredientId")
	private IngredientBean ingredient;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("recipeId")
	@JsonManagedReference
	@JoinColumn(name = "recipeId")
	private RecipeBean recipe;
	
}
