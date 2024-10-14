package com.example.demo.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecipeIngreRepository extends JpaRepository<RecipeIngredientBean, RecipeIngredientId> {

	// select * from recipeIngre r where recipeId = ??
	@Query("select r from RecipeIngredientBean r where r.recipeIngreId.recipeId = :recipeId")
	List<RecipeIngredientBean> findByRecipeId(@Param("recipeId") Integer recipeId);
	
	
	@Query("select r from RecipeIngredientBean r where r.recipeIngreId.ingredientId = :ingreId")
	List<RecipeIngredientBean> findByIngreId(@Param("ingreId") Integer ingreId);
	
	
//	boolean existsByRecipeIdAndIngredientId(Integer recipeId, Integer ingredientId);
	
	boolean existsByRecipeIngreId_RecipeIdAndRecipeIngreId_IngredientId(Integer recipeId, Integer ingredientId);

	@Query("select b from RecipeIngredientBean b where b.recipeIngreId.recipeId = :recipeId and b.recipeIngreId.ingredientId = :ingreId")
	RecipeIngredientBean findByRecipeIdAndIngreId(@Param("recipeId") Integer recipeId, @Param("ingreId") Integer ingreId);
	
}
