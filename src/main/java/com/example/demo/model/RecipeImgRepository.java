package com.example.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;


public interface RecipeImgRepository extends JpaRepository<RecipeImgBean, Integer> {

	@Query("select img from RecipeImgBean img where img.recipeImgId = :imgId and img.recipeBean.recipeId = :recipeId")
	RecipeImgBean findByImgIdAndRecipeId(@Param("imgId") Integer imgId, @Param("recipeId") Integer recipeId);
	
	@Query("select img from RecipeImgBean img where img.recipeBean.recipeId = :recipeId")
	List<RecipeImgBean> findByRecipeId(@Param("recipeId") Integer recipeId);
}
